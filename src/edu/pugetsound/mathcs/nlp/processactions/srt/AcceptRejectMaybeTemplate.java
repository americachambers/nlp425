package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.AcceptTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.AcceptPartTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.RejectTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.RejectPartTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.MaybeTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response which can either agree with, disagree with, or be uncertain
 * whether or not to agree with a statement the user just said. Which is chosen depends on the user's input.
 * This differs from AgreementDisagreementTemplate in that that class more explicitly states whether the
 * computer agrees with the user; this one is less direct and can also be unsure whether it agrees.
 */
public class AcceptRejectMaybeTemplate implements SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        switch(rand.nextInt(4)) {
        case 0:
            return new AcceptTemplate().constructResponseFromTemplate(utterance);
        case 1:
            return new AcceptPartTemplate().constructResponseFromTemplate(utterance);
        case 2:
            return new RejectTemplate().constructResponseFromTemplate(utterance);
        case 3:
            return new RejectPartTemplate().constructResponseFromTemplate(utterance);
        }

        // Else
        return new MaybeTemplate().constructResponseFromTemplate(utterance);
    }

}

