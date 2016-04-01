package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

public class AssessmentAppreciationTemplate implements SemanticResponseTemplate {

    private static final String[] outputs = {
        "That's great",
        "That's so great",
        "Wow",
        "Neat",
        "Cool",
        "Nice",
        "Awesome"
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        if(rand.nextBoolean()) {
            return outputs[rand.nextInt(outputs.length)] + ".";
        } else {
            return outputs[rand.nextInt(outputs.length)] + "!";
        }
    }

}

