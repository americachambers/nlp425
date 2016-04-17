package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * A template for constructing a "yes" response to a user's yes-no question.
 */
public class YesTemplate implements SemanticResponseTemplate {

    private static final HashMap<AMR, String[]> outputs = SemanticResponseTemplate.getResponses("YesTemplate");

    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Random rand = new Random();
        Utterance utterance = convo.getLastUtterance();
        AMR amr = (AMR) outputs.keySet().toArray()[rand.nextInt(outputs.size())];
        return amr.convertAMRToText(outputs.get(amr));
    }

}

