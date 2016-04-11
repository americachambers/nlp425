package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.StatementOpinionTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.StatementNonOpinionTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a general statement.
 * This will directly call for either a nonopinionated statement or an opinionated one, so this class
 * should only be used when you don't know which to pick.
 */
public class StatementTemplate implements SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        if(rand.nextBoolean()) {
            return new StatementOpinionTemplate().constructResponseFromTemplate(utterance);
        } else {
            return new StatementNonOpinionTemplate().constructResponseFromTemplate(utterance);
        }
    }

}

