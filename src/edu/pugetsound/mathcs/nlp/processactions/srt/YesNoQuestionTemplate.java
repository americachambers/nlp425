package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for posing a yes-no question to the user.
 * To form a question, this template will first try to find something the user said to ask about,
 * but if it finds nothing it'll ask a general question about whatever.
 */
public class YesNoQuestionTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo, KBController kb) {
        Random rand = new Random();
        String topic = super.constructDumbResponse(convo, kb);

        if(rand.nextBoolean()) {

            return "Do you like " + topic + "?";
        } else {
            if(topic.equals("me")) {
                if(rand.nextBoolean()) {
                    return "Do you enjoy talking to me?";
                } else {
                    return "Do you think I'm interesting to talk to?";
                }
            } else {
                if(topic.charAt(topic.length() - 1) == 's') {
                    return "Do you think " + topic + " are interesting?";
                } else {
                    return "Do you think " + topic + " is interesting?";
                }
            }
        }
    }

}
