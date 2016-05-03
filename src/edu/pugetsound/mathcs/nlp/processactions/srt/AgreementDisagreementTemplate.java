package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.AgreementTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.DisagreementTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a response which either agrees or disagrees with what the user said.
 * Unlike AcceptRejectMaybeTemplate, this class is explicit about whether it agrees.
 */
public class AgreementDisagreementTemplate extends SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Random rand = new Random();
        Utterance utterance = convo.getLastUtterance();
        if(rand.nextBoolean()) {
            return new AgreementTemplate().constructResponseFromTemplate(convo);
        } else {
            return new DisagreementTemplate().constructResponseFromTemplate(convo);
        }
    }

}
