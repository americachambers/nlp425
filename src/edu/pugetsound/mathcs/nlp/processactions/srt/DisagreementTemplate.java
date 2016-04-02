package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a disagreement with what the user said.
 * This differs from RejectTemplate in that this is explicit in its disagreement, while Reject is implicit.
 * Example responses include "I disagree."
 */
public class DisagreementTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "I don't agree with that.",
        "I don't really agree with that.",
        "I disagree."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}
