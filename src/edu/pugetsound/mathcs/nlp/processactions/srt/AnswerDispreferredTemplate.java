package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

public class AnswerDispreferredTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "I don't really want to talk about that.",
        "I don't really want to talk about it.",
        "Can we talk about something else?",
        "Let's talk about something else."
    };

    @Override
    public static String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}
