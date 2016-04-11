package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

public class NonUnderstandingTemplate implements SemanticResponseTemplate {

    private static final HashMap<String, AMR> outputs = SemanticResponseTemplate.getResponses("NonUnderstandingTemplate");


    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return ((String) outputs.keySet().toArray()[rand.nextInt(outputs.size())]);
    }

}

