
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Zachary Cohan
 */
public class MDP {

    /**
     * @param args the command line arguments
     */
    HashMap<State, Action> policy;
    static ArrayList<State> s = new ArrayList<>();
    static ArrayList<Action> a = new ArrayList<>();
    public static void main(String[] args) {
        s.add(new State("in state 0",0));
        s.add(new State("in state 1",1));
        s.add(new State("in state 2",2));
        s.add(new State("in state 3",3));
        s.add(new State("in state 4",4));
        s.add(new State("in state 5",5));
        
        a.add(new Action("go to state 0",0));
        a.add(new Action("go to state 1",1));
        a.add(new Action("go to state 2",2));
        a.add(new Action("go to state 3",3));
        a.add(new Action("go to state 4",4));
        a.add(new Action("go to state 5",5));
        
        QLearner q = new QLearner(s,a);
        q.train();
        //System.out.println(q.round(0.934586789));
        
        
        
        
    }

}
