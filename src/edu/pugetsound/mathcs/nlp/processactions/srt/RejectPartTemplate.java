package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a template which partly rejects/disagrees with the user's input.
 * Unlike DisagreementTemplate, this is less explicit about its disagreement.
 * Example responses include "Not really." or "Not quite."
 */
public class RejectPartTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "Not really.",
        "Not quite."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}

