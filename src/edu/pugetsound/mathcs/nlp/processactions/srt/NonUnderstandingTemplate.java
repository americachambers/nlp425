package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

public class NonUnderstandingTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "I'm not sure I really understand what you're saying.",
        "I'm not sure I understand what you're saying.",
        "I don't think I'm understanding what you're saying.",
        "I don't understand."
        "I don't really understand."
    };

    @Override
    public static String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}
