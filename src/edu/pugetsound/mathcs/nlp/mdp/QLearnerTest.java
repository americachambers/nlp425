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

    @Before
    public void setUp(){
        hyperVariables = new HyperVariables(GAMMA, EXPLORE);
        mdp = new QLearner(hyperVariables);
    }

    @Test
    public void test(){
        Conversation conversation = new Conversation();
        Utterance utterance1 = new Utterance("Hello");
        Utterance utterance2 =


        while(true) {
            Action action = mdp.train(conversation);
        }

    }


}


