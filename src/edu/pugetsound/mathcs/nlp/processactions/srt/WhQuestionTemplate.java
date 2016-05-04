package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing wh-questions.
 * This class will first attempt to ask something about what the user said, but if it fails to do
 * so it will try to ask a general question about whatever.
 */

public class WhQuestionTemplate implements SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Utterance utterance = convo.getLastUtterance();
        return new QuestionTemplate().constructResponseFromTemplate(convo);
    }

    /**
     * @author Thomas Gagne & Jon Sims
     * @version 04/26/16
     * A template for answering yes-no questions posed by the user.
     * This class will call the knowledge base to determine the answer, then return the
     * appropriate answer.
     */
    public static class YesNoAnswerTemplate implements SemanticResponseTemplate {

        @Override
        public String constructResponseFromTemplate(Conversation convo) {
            KBController kb = new KBController();
            Utterance utterance = convo.getLastUtterance();
            if(kb.yesNo(utterance.firstOrderRep)) {
                return new NoTemplate().constructResponseFromTemplate(convo);
            } else {
                return new YesTemplate().constructResponseFromTemplate(convo);
            }
        }

    }
}
