package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a non-opinionated response to something the user said.
 * This template should be used for answering non-yes-no questions and for making general statements.
 */
public class StatementNonOpinionTemplate implements SemanticResponseTemplate {

    private static final HashMap<String, AMR> outputs = SemanticResponseTemplate.getResponses("StatementNonOpinionTemplate");


    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return ((String) outputs.keySet().toArray()[rand.nextInt(outputs.size())]);
    }

}

