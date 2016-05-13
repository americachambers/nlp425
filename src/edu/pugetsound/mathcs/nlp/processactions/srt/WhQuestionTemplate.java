package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.QuestionTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing wh-questions.
 * This class will first attempt to ask something about what the user said, but if it fails to do
 * so it will try to ask a general question about whatever.
 */
public class WhQuestionTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo, KBController kb) {
        String topic = super.constructDumbResponse(convo, kb);
        Random rand = new Random();
        int format = rand.nextInt(4);

        switch(format) {
        case 0:
            return "What do you think about " + topic + "?";
        case 1:
            return "What are your thoughts on " + topic + "?";
        case 2:
            return "Why do you feel the way you do about " + topic + "?";
        case 3:
            if(topic.charAt(topic.length() - 1) == 's') {
                return "What are " + topic + "?";
            } else {
                return "What is " + topic + "?";
            }
        default:
            return "Why is that?";
        }

    }
}
