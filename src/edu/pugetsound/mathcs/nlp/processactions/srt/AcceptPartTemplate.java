package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.ActionProcessor;



/**
 * @author Thomas Gagne
 * A template for constructing a response which mostly agrees with what the user said.
 * Responses include "I guess so" or "Something like that."
 */
public class AcceptPartTemplate implements SemanticResponseTemplate {

    private static final HashMap<String, AMR> outputs = SemanticResponseTemplate.getResponses("AcceptPartTemplate");


    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return ((String) outputs.keySet().toArray()[rand.nextInt(outputs.size())]);
    }

}

