package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response which reformulates or summarizes what the user said.
 * If the user said "Me, Jack, and Jill all drove to the bar", a response might be
 * "Ok, you all went to the bar."
 */
public class ReformulateTemplate implements SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        if(utterance.utterance != null) {
            return utterance.utterance;
        } else {
            // return "Yeah, what you said.";
        }
    }

}

