package edu.pugetsound.mathcs.nlp.mdp;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

public class State {

    int id;
    DialogueActTag DATag;
    //ArrayList<Action> possActs;

    public State(DialogueActTag DATag, int id) {
        this.DATag = DATag;
        this.id = id;
    }

    public DialogueActTag getDATag() {
        return DATag;
    }
    
    public int id()
    {
        return id;
    }
}
