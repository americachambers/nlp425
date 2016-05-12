package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.YesTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.NoTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.MaybeTemplate;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.kb.PrologStructure;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for answering yes-no questions posed by the user.
 * This class will call the knowledge base to determine the answer, then return the
 * appropriate answer.
 */
public class YesNoAnswerTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo, KBController kb) {
        Utterance utterance = convo.getLastUtterance();
        if(utterance != null && utterance.firstOrderRep != null && kb != null) {
            if(kb.yesNo(utterance.firstOrderRep)) {
                return new YesTemplate().constructDumbResponse(convo, kb);
            } else {
                return new NoTemplate().constructDumbResponse(convo, kb);
            }
        } else {
            return new MaybeTemplate().constructDumbResponse(convo, kb);
        }
    }

}
