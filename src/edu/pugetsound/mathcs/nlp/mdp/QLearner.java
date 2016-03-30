
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Zachary Cohan
 */
public class QLearner {

    ArrayList<State> states;
    ArrayList<Action> actions;
    //int[][] r_table;
    double[][] q_table;
    double GAMMA;
    int EXPLORE;
    int ANNEAL;
    public QLearner(List<State> states, List<Action> actions) {
        if (states.size() < 1 || actions.size() < 1) {
            throw new IllegalArgumentException();
        }
        GAMMA = HV.GAMMA;
        EXPLORE = HV.EXPLORE;
        ANNEAL = HV.ANNEAL;
        
        this.states = (ArrayList<State>) states;
        this.actions = (ArrayList<Action>) actions;
        //r_table = new int[states.size()][actions.size()];
        q_table = new double[states.size()][actions.size()];
    }

    public void train() {
        //Scanner in = new Scanner(System.in);

        int state = 1;

        
        int reward;
        int choice;
        double alpha;
        int newState = 0;

        Random r = new Random();
        
            alpha = (double) ANNEAL / (double) EXPLORE;//this is our alpha value, it goes down as ANNEAL goes down
            //expore vs. exploit
            
            int e = r.nextInt(EXPLORE);//pick a random value between [0,1000)
            if (e < ANNEAL) {//while e is less than ANNEAL, we will explore
                choice = r.nextInt(actions.size());

//                System.out.println("EXPLORE: I am " + states.get(state).DATag + " and would like to " + actions.get(choice).DATag);
//                System.out.println("is this valid? (if not valid move put -1, -2 to quit) what is my reward?");
                reward = gr(state, choice);
                //reward = in.nextInt();
                
//                } else if (reward == -1) {//if our move is invalid, update the tables to show that and try the loop again
//                    r_table[state][choice] = reward;
//                    q_table[state][choice] = reward;
//                    continue;
//                }
                newState = choice;//if we picked a valid move then our s' becomes whatever choice we made

                double maxAPrime = q_table[newState][0];//initialize our max to the first action for our given new state
                for (int i = 1; i < q_table[newState].length; i++) {//go thru all following actions
                    if (q_table[newState][i] > maxAPrime) {
                        maxAPrime = q_table[newState][i];//if there's a better value, update maxA'
                    }
                }

                
                q_table[state][choice] = q_table[state][choice] + (alpha) * (((double)reward + (GAMMA * maxAPrime)) - q_table[state][choice]);

            } else {//explore
                //default our choice to the first action
                choice = 0;

                //go thru all choices to see if there's a better choice
                for (int i = 1; i < q_table[state].length; i++) {
                    if (q_table[state][i] > q_table[state][choice]) {
                        choice = i;
                    }
                }

//                System.out.println("EXPLOIT: I am " + states.get(state).DATag + " and would like to " + actions.get(choice).DATag);
//                System.out.println("is this valid? what is my reward?");
                //reward = in.nextInt();
                reward = gr(state, choice);
//                if (reward == -2) {
//                    break;
//                } //if the move is invalid, update the tables to show that and continue the loop
//                else if (reward == -1) {
//                    r_table[state][choice] = reward;
//                    q_table[state][choice] = reward;
//                    continue;
//                }
                //our s' is whatever choice we made
                newState = choice;

                //default our best choice to the first action
                double maxAPrime = q_table[newState][0];
                //check remaining actions to see if there's something better
                for (int i = 1; i < q_table[newState].length; i++) {
                    if (q_table[newState][i] > maxAPrime) {
                        maxAPrime = q_table[newState][i];
                    }
                }

                q_table[state][choice] = q_table[state][choice] + (alpha) * (((double) reward + (GAMMA * maxAPrime)) - q_table[state][choice]);

            }

            state = newState;
            ANNEAL--;
//            System.out.println(ANNEAL);
//            if (ANNEAL == 0) {
//                break;
//            }
        

//        printPolicy();
    }

//    public static HashMap<State, Action> createPolicy()
//    {
//       return null; 
//    }
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

    private int gr(int state, int choice) {
        Scanner in = new Scanner(System.in);
        System.out.println("I am in state" + states.get(state).DATag + " and would like to use a " + actions.get(choice).DATag);
        System.out.println("On a scale of 1-5, how accurate is this move?");
        return in.nextInt()-3;
                

//        if (s == a) {
//            return -1;
//        } else if (s == 2 && a == 0) {
//            return 10;
//        } else if (s == 3 && a == 0) {
//            return 10;
//        } else if (s == 3 && a == 1) {
//            return 0;
//        } else if (s == 1 && a == 2) {
//            return 0;
//        } else if (s == 4 && a == 2) {
//            return 0;
//        } else if (s == 2 && a == 3) {
//            return 0;
//        } else if (s == 0 && a == 4) {
//            return 0;
//        } else if (s == 5 && a == 4) {
//            return 0;
//        } else if (s == 1 && a == 5) {
//            return 0;
//        } else if (s == 5 && a == 2) {
//            return 0;
//        } else {
//            return -1;
//        }
    }

    protected double round(double a) {
        double b = a * 10000;
        b = (int) b;
        b = b / 10000;
        return b;

    }
}
