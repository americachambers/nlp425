package edu.pugetsound.mathcs.nlp.mdp;

/**
 * Created by dgwilliams on 4/18/2016.
 */

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QLearnerTest {
    protected static QLearner mdp;

    /**
     * The class which sets the initial settings for the MDP
     */
    protected static HyperVariables hyperVariables;

    /**
     * The discounted value for the Markov Decision Process
     */
    protected static final double GAMMA = 0.1;

    /**
     * Related to the duration and likelihood of exploring vs. exploiting for the MDP
     * A higher value corresponds to a longer exploration phase
     */
    protected static final int EXPLORE = 1000;

    /**
     * multiple conversations of difference lengths
     */
    protected Conversation conversation1= new Conversation();
    protected Conversation conversation2= new Conversation();
    protected Conversation conversation3= new Conversation();
    protected Conversation conversation4= new Conversation();

    /**
     * multiple utterances added to the conversations
     */
    Utterance utterance1 = new Utterance("Hello");
    Utterance utterance2 = new Utterance("Hi");
    Utterance utterance3 = new Utterance("How are you?");
    Utterance utterance4 = new Utterance("I'm doing just dandy");

    @Before
    public void setUp(){
        hyperVariables = new HyperVariables(GAMMA, EXPLORE);
        mdp = new QLearner(hyperVariables);
    }

    @Test
    public void test(){

        conversation1.addUtterance(utterance1);

        conversation2.addUtterance(utterance1);
        conversation2.addUtterance(utterance2);

        conversation2.addUtterance(utterance1);
        conversation2.addUtterance(utterance2);
        conversation2.addUtterance(utterance3);

        conversation2.addUtterance(utterance1);
        conversation2.addUtterance(utterance2);
        conversation2.addUtterance(utterance3);
        conversation2.addUtterance(utterance4);

        Action action1 = mdp.train(conversation1);

        Action action2 = mdp.train(conversation2);

        Action action3 = mdp.train(conversation3);

        Action action4 = mdp.train(conversation4);
    }
}


