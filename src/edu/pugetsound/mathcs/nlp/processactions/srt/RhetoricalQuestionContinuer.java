package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response to a user's rhetorical question or statement.
 * Example responses include "Is that right?" or "Oh, really?"
 */
public class RhetoricalQuestionContinuer implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "Is that right?",
        "Really?",
        "Oh, really?",
        "Is that right?",
        "You're kidding me."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}

