package edu.pugetsound.mathcs.nlp.mdp;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;
import edu.pugetsound.mathcs.nlp.util.Logger;
import edu.pugetsound.mathcs.nlp.util.PathFormat;

import java.io.*;
import java.util.*;

/**
 * @author Zachary Cohan and Damon Williams
 */
public class QLearner {

    private HashMap<State, Integer> states;
    private HashMap<Integer, State> ids;
    private ArrayList<Action> actions;
    private double[][] q_table;
    private double GAMMA;
    private int EXPLORE;
    private int ANNEAL;

    private int lState;//last state, last action, last reward
    private int lAction;
    private int lReward;
    private double maxAPrime;
    private double alpha;
    protected Random r;

    private static final String INPUT_PATH = PathFormat.absolutePathFromRoot("models/qlearner/qlearner");

    //private final boolean DEBUG_MODE = false; //DEBUG_MODE is 1 when we want to print debug information

    /**
     * Constructs the original QLearner: defines the states and actions that are
     * possible, and initializes the QTable based on those states and actions.
     *
     * @param h - the {@link HyperVariables} class is the class which configures
     * the variables for the QLearner
     */
    public QLearner(HyperVariables h, boolean readFromFile) {
        //create states and actions
        states = new HashMap<>();
        ids = new HashMap<>();
        actions = new ArrayList<>();
        r = new Random();
        int id = 0;

        //adds all possible actions to the actions arraylist
        for (ResponseTag dialogueActTag : ResponseTag.values()) {
            this.actions.add(new Action(dialogueActTag, id));
            id++;
        }

        //starting with the null state, adds all states to the state Arraylist
        id = 1;
        this.states.put(new State(DialogueActTag.NULL, DialogueActTag.NULL), 0);
        this.ids.put(0, new State(DialogueActTag.NULL, DialogueActTag.NULL));
        for (DialogueActTag dialogueActTag : DialogueActTag.values()) {
            for (DialogueActTag dialogueActTag2 : DialogueActTag.values()) {
                if (dialogueActTag2 == DialogueActTag.NULL) {
                    //if our 2nd DA Tag is null, then don't do anything
                } else {
                    this.states.put(new State(dialogueActTag, dialogueActTag2), id);
                    this.ids.put(id, new State(dialogueActTag, dialogueActTag2));
                    id++;
                }
            }
        }
        //check if state and actions have been created
        //this shouldn't ever be triggered as of right now
        if (states.size() < 1 || actions.size() < 1) {
            throw new IllegalArgumentException();
        }

        q_table = new double[states.size()][actions.size()];

        if (readFromFile) {
            readFromFile();
        }else {
            GAMMA = h.getGamma();
            EXPLORE = h.getExplore();
            ANNEAL = h.getExplore();
        }
    }

    public Action train(Conversation conversation) {

        double alpha = (double) ANNEAL / (double) EXPLORE; //this is the alpha value, it goes down as ANNEAL goes down

        if (Logger.debug()) {
            System.out.println("Anneal val: " + ANNEAL);
            System.out.println("Explore val: " + EXPLORE);
            System.out.println("alpha val(anneal/explore): " + alpha);
        }
        List<Utterance> utterances = conversation.getConversation();

        DialogueActTag olderDAtag;
        //CHANGE THIS, ITS BROKEN
        if (utterances.size() == 0) {
            return new Action(ResponseTag.CONVENTIONAL_OPENING, -1);
        } else if (utterances.size() == 2) {
            olderDAtag = DialogueActTag.NULL;
        } else {
            olderDAtag = utterances.get(utterances.size() - 3).daTag;
        }
        DialogueActTag mostRecentDAtag = utterances.get(utterances.size() - 1).daTag;
        int stateIndex = 0;

        //search through states and determine which state we are in.
        stateIndex = states.get(new State(olderDAtag, mostRecentDAtag));
        if (Logger.debug()) {
            System.out.println("current state index: " + stateIndex);
            System.out.println("state it represents: " + new State(olderDAtag, mostRecentDAtag));
        }

        //this updates the Q(s,a) where s is the previous state and a is the previous action
        //this must be done in order to sync reward functionality
        if (Logger.debug()) {
            System.out.println("Updating previous states");

        }

        if (mostRecentDAtag != null) {
            //last state, last action, aprime, alpha reward;
            updateQTable(lState, lAction, maxAPrime, this.alpha, lReward);
        }

        int choice;
        //A random variable used to choose if we are exploring or exploiting.

        //explore vs. exploit
        int e = r.nextInt(EXPLORE);//pick a random value between [0,1000)
        if (e < ANNEAL) {
            //if e is less than ANNEAL, we will explore
            choice = r.nextInt(actions.size());//chooses random action
            lReward = rateActionChoice(stateIndex, choice);

            maxAPrime = bestResponseValue(stateIndex);
        } else {
            //exploit
            choice = 0; //default our choice to the first action
            //go thru all choices to see if there's a better choice
            for (int i = 1; i < q_table[stateIndex].length; i++) {
                if (q_table[stateIndex][i] > q_table[stateIndex][choice]) {
                    choice = i;
                }
            }
            lReward = rateActionChoice(stateIndex, choice);
            maxAPrime = bestResponseValue(stateIndex);
        }
        lAction = choice;
        lState = stateIndex;
        this.alpha = alpha;
        ANNEAL--;

        //return the action that we decided to take to the processing actions team.
        return actions.get(choice);
    }

    private void printPolicy() {
        for (int i = 0; i < q_table.length; i++) {
            int act = 0;
            for (int j = 0; j < q_table[i].length; j++) {
                if (q_table[i][j] > q_table[i][act]) {
                    act = j;
                }
            }
            System.out.println("For STATE " + i + ", " + actions.get(act).DATag);
        }
        System.out.println();
        System.out.println("Q TABLE:");

        for (int i = 0; i < q_table.length; i++) {
            for (int j = 0; j < q_table[i].length; j++) {
                System.out.print(round(q_table[i][j]) + "\t\t");
            }
            System.out.println();
        }

    }

    private int rateActionChoice(int state, int choice) {
        Scanner in = new Scanner(System.in);
        int r = -1;
        System.out.println("I am in state <" + ids.get(state).DATag1 + "," + ids.get(state).DATag2 + "> and will respond with a " + actions.get(choice).DATag);
        System.out.println("On a scale of 1-5, how accurate is this response?");

        try {
            r = in.nextInt();
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString() + "\n" + "Scanner is null?");
        }

        if (r < 1 || r > 5) {
            System.err.println("Enter values between in the range [1,5]; no decimals");
            rateActionChoice(state, choice);
        }
        return r - 3;
    }

    protected double round(double a) {
        double b = a * 10000;
        b = (int) b;
        b = b / 10000;
        return b;
    }

    private double bestResponseValue(int ns) {
        double maxAPrime = q_table[ns][0];
        for (int i = 1; i < q_table[ns].length; i++) {
            if (q_table[ns][i] > maxAPrime) {
                maxAPrime = q_table[ns][i];
            }
        }
        return maxAPrime;
    }


    private void updateQTable(int state, int action, double aPrime, double alpha, int reward) {
        q_table[state][action]
                = q_table[state][action]
                + (alpha) * (((double) reward
                + (GAMMA * aPrime)) - q_table[state][action]);
    }

    public boolean saveToFile(){
        File file = new File(INPUT_PATH);
        FileWriter writer = null;

        try {
            writer = new FileWriter(file);
            writer.append(String.valueOf(GAMMA)
                    +","+ String.valueOf(ANNEAL)
                    +","+ String.valueOf(EXPLORE));
            for(int i = 0; i<q_table.length; i++){
                writer.append("\n");
                for(int j = 0; j< q_table[i].length; j++){
                    if(j == q_table[i].length-1){
                        writer.append(String.valueOf(q_table[i][j]));
                    }else{
                        writer.append(String.valueOf(q_table[i][j]) + ",");
                    }
                }
            }
        }catch(IOException e) {
            System.err.println("Couldn't write to file");
            e.printStackTrace();
            return false;
        }
        try{
            writer.flush();
            writer.close();
        }catch(IOException e){
            System.out.println("error in QLearner - saveToFile()");
            e.printStackTrace();
        }
        return true;
    }

    private boolean readFromFile(){
        final String delimiter = ",";

        try{
            BufferedReader fileReader = new BufferedReader(new FileReader(INPUT_PATH));

            //Read first line, which contains instance variables: GAMMA, ANNEAL, and EXPLORE
            String line = fileReader.readLine();
            String[] tokens = line.split(delimiter);
            GAMMA = Double.parseDouble(tokens[0]);
            ANNEAL = Integer.parseInt(tokens[1]);
            EXPLORE = Integer.parseInt(tokens[2]);

            for(int i = 0; i<q_table.length; i++){
                line = fileReader.readLine();
                tokens = line.split(delimiter);
                for(int j=0; j< q_table[i].length; j++){
                    q_table[i][j] = Double.parseDouble(tokens[j]);
                }
            }
        }catch(IOException e){
            System.err.println("Couldn't read from file");
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
