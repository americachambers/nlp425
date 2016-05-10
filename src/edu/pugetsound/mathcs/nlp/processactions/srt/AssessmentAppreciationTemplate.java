package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.AMRParser;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a positive response to what the user said.
 * Example response include "That's great." or "Neat!"
 */
public class AssessmentAppreciationTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo) {
        Random rand = new Random();
        String result = super.constructDumbResponse(convo);

        if(rand.nextBoolean()) {
            return result + ".";
        } else {
            return result + "!";
        }
    }

}
