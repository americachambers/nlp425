package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.YesTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.NoTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.IndeterminateResponseTemplate;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.kb.PrologStructure;

/**
 * @author Thomas Gagne & Jon Sims
 * A template for answering yes-no questions posed by the user.
 * This class will call the knowledge base to determine the answer, then return the appropriate answer.x
 */
public class YesNoAnswerTemplate implements SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        KBController kb = new KBController();
        Utterance utterance = convo.getLastUtterance();
        for(PrologStructure ps : utterance.firstOrderRep) {
            if(!kb.yesNo(ps)) {
                return new NoTemplate().constructResponseFromTemplate(convo);
            }
        }

        return new YesTemplate().constructResponseFromTemplate(convo);
    }

}

