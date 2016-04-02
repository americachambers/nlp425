package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response which agrees with what the user said.
 * This differs from AcceptTemplate in that this class is explicit about whether it agrees or not,
 * whereas AcceptTemplate is more implicit.
 * Example responses include "I agree with that."
 */
public class AgreementTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "I agree with that.",
        "I can agree with that."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}
