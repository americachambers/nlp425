package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a response which reformulates or summarizes what the user said.
 * If the user said "Me, Jack, and Jill all drove to the bar", a response might be
 * "Ok, you all went to the bar."
 */
public class ReformulateTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo, KBController kb) {
        Utterance utterance = convo.getLastUtterance();
        if(utterance.utterance != null) {
            return utterance.utterance;
        } else {
            return "Yeah, what you said.";
        }
    }

}
