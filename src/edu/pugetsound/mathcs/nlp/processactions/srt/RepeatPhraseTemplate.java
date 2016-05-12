package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a response which isolates a noun phrase in the user's input,
 * to signal incredularity or interest.
 * For example, the user says "We ate the whole cat", and so we might respond with "The whole cat."
 */
public class RepeatPhraseTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo, KBController kb) {
        Utterance utterance = convo.getLastUtterance();

        if(utterance != null && utterance.utterance != null) {
            return utterance.utterance;
        } else {
            // Not perfect since it doesn't really match this template,
            // but I can't think of an alternative
            return new NonUnderstandingTemplate().constructDumbResponse(convo, kb);
        }
    }
}
