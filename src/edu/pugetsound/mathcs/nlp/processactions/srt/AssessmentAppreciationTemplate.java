package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * A template for constructing a positive response to what the user said.
 * Example response include "That's great." or "Neat!"
 */
public class AssessmentAppreciationTemplate implements SemanticResponseTemplate {

    private HashMap<AMR, String[]> outputs = SemanticResponseTemplate.responses.get(this.getClass().getName());


    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Random rand = new Random();
        Utterance utterance = convo.getLastUtterance();
        if(rand.nextBoolean()) {
            return ((String) outputs.keySet().toArray()[rand.nextInt(outputs.size())]) + ".";
        } else {
            return ((String) outputs.keySet().toArray()[rand.nextInt(outputs.size())]) + "!";
        }
    }

}

