package edu.pugetsound.mathcs.nlp.processactions.srt;

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
        String topic = super.constructDumbResponse(convo, kb);
        return "Do you like " + topic + "?";
    }

}
