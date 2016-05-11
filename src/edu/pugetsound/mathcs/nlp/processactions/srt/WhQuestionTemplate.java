package edu.pugetsound.mathcs.nlp.processactions.srt;

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
        return new QuestionTemplate().constructDumbResponse(convo, kb);
    }
}
