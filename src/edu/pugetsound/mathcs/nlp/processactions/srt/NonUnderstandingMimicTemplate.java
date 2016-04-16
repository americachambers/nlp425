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
 * @author Thomas Gagne
 * A template for expressing nonunderstanding of what the user said.
 * This differs from NonUnderstandingTemplate in that this response is more explicit about what part
 * it did not understand by referencing something in the user's input.
 * For example, if the user says "I enjoy gooflagggin" this would return "Gooflagggin?"
 */
public class NonUnderstandingMimicTemplate implements SemanticResponseTemplate {

    // This template is like the RepeatPhraseTemplate, but adds a question mark to the end, to signal confusion

    private static final HashMap<AMR, String[]> outputs = SemanticResponseTemplate.getResponses("NonUnderstandingMimicTemplate");


    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Random rand = new Random();
        Utterance utterance = convo.getLastUtterance();
        AMR amr = (AMR) outputs.keySet().toArray()[rand.nextInt(outputs.size())];
        return amr.convertAMRToText(outputs.get(amr));
    }

}

