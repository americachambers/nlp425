package edu.pugetsound.mathcs.nlp.mdp;

import edu.pugetsound.mathcs.nlp.processactions.ExtendedDialogueActTag;

/**
 *
 * @author Zachary Cohan
 */
public class Action {

    //State init;

    int id;
    ExtendedDialogueActTag DATag;
        //ArrayList<Action> possActs;

    public Action(ExtendedDialogueActTag DATag, int id) {
        this.DATag = DATag;
        this.id = id;
    }

    public ExtendedDialogueActTag getDATag() {
        return DATag;
    }
    
    public int id()
    {
        return id;
    }
}
