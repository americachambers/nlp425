package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response which positively akcnowledges/agrees with what the user said.
 * This differs from AgreementTemplate, which explicitly states that the computer agrees with them.
 * Example responses include "Yeah." or "Ok."
 */
public class AcceptTemplate implements SemanticResponseTemplate {


    private static final HashMap<String, AMR> outputs = SemanticResponseTemplate.getResponses("AcceptTemplate");


    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return (String) outputs.keySet().toArray()[rand.nextInt(outputs.size())];
    }

}

