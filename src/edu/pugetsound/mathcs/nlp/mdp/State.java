package edu.pugetsound.mathcs.nlp.mdp;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

public class State {

    
    int id;
    DialogueActTag DATag1;
    DialogueActTag DATag2;
    //ArrayList<Action> possActs;

    public State(DialogueActTag DATag1, DialogueActTag DATag2) {
        this.DATag1 = DATag1;
        this.DATag2 = DATag2;
        //this.id = id;
    }

    public DialogueActTag getEarlyDATag() {
        return DATag1;
    }
    
    public DialogueActTag getRecentDATag() {
        return DATag2;
    }
    
    public int id()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        return DATag1.toString().hashCode() + DATag2.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (this.DATag1 != other.DATag1) {
            return false;
        }
        if (this.DATag2 != other.DATag2) {
            return false;
        }
        return true;
    }

}
