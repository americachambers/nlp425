package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a sympathetic statement towards something the user said.
 * Example responses include "I'm sorry." or "Sorry."
 */
public class SympathyTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "I'm sorry to hear that.",
        "I'm sorry.",
        "Sorry.",
        "You have my sympathy."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}

