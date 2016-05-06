package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.AMRParser;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * The catch-all class for constructing questions.
 * This class will attempt to ask a question about something the user said, and if failing at that
 * will attempt to ask a question about basically anything.
 */
public class QuestionTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo) {
        String result = super.constructDumbResponse(convo);
        Random rand = new Random();

        if(rand.nextBoolean()) {
            return "What do you think about " + result + "?";
        } else {
            return "What are your thoughts on " + result + "?";
        }
    }

}
