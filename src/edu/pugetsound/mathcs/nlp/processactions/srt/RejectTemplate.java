package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response which rejects/disagrees with what the user said.
 * This is less explicit in its disagreement than DisagreementTemplate
 * Example responses include "No." or "Nah."
 */
public class RejectTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "Nah.",
        "Not really.",
        "No.",
        "Nope."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}
