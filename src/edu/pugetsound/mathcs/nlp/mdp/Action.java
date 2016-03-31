package edu.pugetsound.mathcs.nlp.mdp;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

/**
 *
 * @author Zachary Cohan
 */
public class Action {

    //State init;

//    int id;
    DialogueActTag DATag;
        //ArrayList<Action> possActs;

    public Action(DialogueActTag DATag) {
        this.DATag = DATag;
//        this.id = id;
    }

    public DialogueActTag getDATag() {
        return DATag;
    }
}
