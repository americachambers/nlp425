package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a response which positively akcnowledges/agrees with what the user
 * said.
 * This differs from AgreementTemplate, which explicitly states that the computer agrees with them.
 * Example responses include "Yeah." or "Ok."
 */
public class AcceptTemplate implements SemanticResponseTemplate {

    private HashMap<AMR, String[]> outputs =
        SemanticResponseTemplate.responses.get(this.getClass().getName());

    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Random rand = new Random();
        Utterance utterance = convo.getLastUtterance();
        AMR amr = (AMR) outputs.keySet().toArray()[rand.nextInt(outputs.size())];
        return amr.convertAMRToText(outputs.get(amr));
    }

}
