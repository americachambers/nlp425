package edu.pugetsound.mathcs.nlp.mdp;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class QLearner {

    ArrayList<State> states;
    ArrayList<Action> actions;
    //int[][] r_table;
    double[][] q_table;
    double GAMMA;
    int EXPLORE;
    int ANNEAL;

    public QLearner() {
        //create states and actions
        for(DialogueActTag dialogueActTag : DialogueActTag.values()){
            this.states.add(new State(dialogueActTag));
            this.actions.add(new Action(dialogueActTag));
        }

        if (states.size() < 1 || actions.size() < 1) {
            throw new IllegalArgumentException();
        }

        GAMMA = HV.GAMMA;
        EXPLORE = HV.EXPLORE;
        ANNEAL = HV.ANNEAL;

        q_table = new double[states.size()][actions.size()];
    }

    public Action train(Conversation conversation) {

        List<Utterance> utterances = conversation.getConversation();
        DialogueActTag mostRecentDAtag = utterances.get(utterances.size()).daTag;
        int stateIndex = 1;

        for(int i = 0; i<states.size()-1; i++){
            if(states.get(i).DATag.equals(mostRecentDAtag)){
                stateIndex = i;
            }
        }

        int reward;
        int choice;
        double alpha;
        int newStateIndex = 0;

        Random r = new Random();
        
            alpha = (double) ANNEAL / (double) EXPLORE;//this is our alpha value, it goes down as ANNEAL goes down
            //explore vs. exploit
            
            int e = r.nextInt(EXPLORE);//pick a random value between [0,1000)
            if (e < ANNEAL) {//while e is less than ANNEAL, we will explore
                choice = r.nextInt(actions.size());
//                System.out.println("EXPLORE: I am " + states.get(state).DATag + " and would like to " + actions.get(choice).DATag);
//                System.out.println("is this valid? (if not valid move put -1, -2 to quit) what is my reward?");
                reward = rateActionChoice(stateIndex, choice);
                newStateIndex = choice;//if we picked a valid move then our s' becomes whatever choice we made

                double maxAPrime = q_table[newStateIndex][0];//initialize our max to the first action for our given new state
                for (int i = 1; i < q_table[newStateIndex].length; i++) {//go thru all following actions
                    if (q_table[newStateIndex][i] > maxAPrime) {
                        maxAPrime = q_table[newStateIndex][i];//if there's a better value, update maxA'
                    }
                }

                
                q_table[stateIndex][choice] = q_table[stateIndex][choice] + (alpha) * (((double)reward + (GAMMA * maxAPrime)) - q_table[stateIndex][choice]);

            } else {//exploit
                //default our choice to the first action
                choice = 0;

                //go thru all choices to see if there's a better choice
                for (int i = 1; i < q_table[stateIndex].length; i++) {
                    if (q_table[stateIndex][i] > q_table[stateIndex][choice]) {
                        choice = i;
                    }
                }

//                System.out.println("EXPLOIT: I am " + states.get(state).DATag + " and would like to " + actions.get(choice).DATag);
//                System.out.println("is this valid? what is my reward?");
                reward = rateActionChoice(stateIndex, choice);
                newStateIndex = choice;

                //default our best choice to the first action
                double maxAPrime = q_table[newStateIndex][0];
                //check remaining actions to see if there's something better
                for (int i = 1; i < q_table[newStateIndex].length; i++) {
                    if (q_table[newStateIndex][i] > maxAPrime) {
                        maxAPrime = q_table[newStateIndex][i];
                    }
                }

                q_table[stateIndex][choice] = q_table[stateIndex][choice] + (alpha) * (((double) reward + (GAMMA * maxAPrime)) - q_table[stateIndex][choice]);

            }

            stateIndex = newStateIndex;
            ANNEAL--;
        
//        printPolicy();
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
        System.out.println("I am in state" + states.get(state).DATag + " and would like to use a " + actions.get(choice).DATag);
        System.out.println("On a scale of 1-5, how accurate is this move?");
        return in.nextInt()-3;
    }

    protected double round(double a) {
        double b = a * 10000;
        b = (int) b;
        b = b / 10000;
        return b;

    }
}
