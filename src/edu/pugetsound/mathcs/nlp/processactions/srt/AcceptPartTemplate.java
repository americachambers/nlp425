package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response which mostly agrees with what the user said.
 * Responses include "I guess so" or "Something like that."
 */
public class AcceptPartTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "I guess so.",
        "I probably can agree with that.",
        "That's probably right.",
        "Something like that.",
        "I guess, yeah."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}

