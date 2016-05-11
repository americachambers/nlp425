package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.AgreementTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.DisagreementTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a response which either agrees or disagrees with what the user said.
 * Unlike AcceptRejectMaybeTemplate, this class is explicit about whether it agrees.
 * This class uses a similar approach as YesNoAnswerTemplate to determine its thoughts
 */
public class AgreementDisagreementTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo, KBController kb) {
        Utterance utterance = convo.getLastUtterance();
        if(utterance != null && utterance.firstOrderRep != null && kb != null &&
           kb.yesNo(utterance.firstOrderRep)) {
            return new AgreementTemplate().constructDumbResponse(convo, kb);
        } else {
            return new DisagreementTemplate().constructDumbResponse(convo, kb);
        }
    }

}
