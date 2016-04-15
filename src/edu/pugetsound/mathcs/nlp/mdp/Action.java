package edu.pugetsound.mathcs.nlp.mdp;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;

/**
 *
 * @author Zachary Cohan
 */
public class Action {

    //State init;

//    int id;
    ResponseTag DATag;
        //ArrayList<Action> possActs;

    public Action(ResponseTag DATag) {
        this.DATag = DATag;
//        this.id = id;
    }

    public ResponseTag getDATag() {
        return DATag;
    }
}
