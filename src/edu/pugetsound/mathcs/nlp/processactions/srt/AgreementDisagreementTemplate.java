package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.AgreementTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.DisagreementTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response which either agrees or disagrees with what the user said.
 * Unlike AcceptRejectMaybeTemplate, this class is explicit about whether it agrees.
 */
public class AgreementDisagreementTemplate implements SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        if(rand.nextBoolean()) {
            return new AgreementTemplate().constructResponseFromTemplate(utterance);
        } else {
            return new DisagreementTemplate().constructResponseFromTemplate(utterance);
        }
    }

}

