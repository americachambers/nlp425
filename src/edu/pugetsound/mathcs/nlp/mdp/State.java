package edu.pugetsound.mathcs.nlp.mdp;

import java.util.ArrayList;
import java.util.List;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

public class State {
//        int id;
        DialogueActTag DATag;
        //ArrayList<Action> possActs;
        
        public State(DialogueActTag DATag)
        {
            this.DATag = DATag;
//            this.id = id;
        }
        
//        public void addAction(Action a){
//            possActs.add(a);
//        }
//        
//        public void addActions(List<Action> a)
//        {
//            possActs.addAll(a);
//        }
}
