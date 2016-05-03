package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.AMRParser;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.YesTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.NoTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.IndeterminateResponseTemplate;
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
    public String constructDumbResponse(Conversation convo) {
        KBController kb = new KBController();
        Utterance utterance = convo.getLastUtterance();
        if(kb.yesNo(utterance.firstOrderRep)) {
            return new NoTemplate().constructDumbResponse(convo);
        } else {
            return new YesTemplate().constructDumbResponse(convo);
        }
    }

}
