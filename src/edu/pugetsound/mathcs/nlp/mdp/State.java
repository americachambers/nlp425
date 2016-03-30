package edu.pugetsound.mathcs.nlp.mdp;

import java.util.ArrayList;
import java.util.List;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zachary Cohan
 */

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
