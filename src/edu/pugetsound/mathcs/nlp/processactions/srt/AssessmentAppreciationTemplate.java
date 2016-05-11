package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.kb.KBController;

import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a positive response to what the user said.
 * Example response include "That's great." or "Neat!"
 */
public class AssessmentAppreciationTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo, KBController kb) {
        Random rand = new Random();
        String result = super.constructDumbResponse(convo, kb);

        if(rand.nextBoolean()) {
            return result + ".";
        } else {
            return result + "!";
        }
    }

}
