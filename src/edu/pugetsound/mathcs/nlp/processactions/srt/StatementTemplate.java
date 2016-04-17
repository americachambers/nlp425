package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;
import java.util.List;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.StatementOpinionTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.StatementNonOpinionTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * A template for constructing a general statement.
 * This will directly call for either a nonopinionated statement or an opinionated one, so this class
 * should only be used when you don't know which to pick.
 */
public class StatementTemplate implements SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Random rand = new Random();
        Utterance utterance = convo.getLastUtterance();
        if(rand.nextBoolean()) {
            return new StatementOpinionTemplate().constructResponseFromTemplate(convo);
        } else {
            return new StatementNonOpinionTemplate().constructResponseFromTemplate(convo);
        }
    }

}

