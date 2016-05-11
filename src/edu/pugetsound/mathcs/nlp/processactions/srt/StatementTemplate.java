package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;


import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.kb.KBController;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.StatementOpinionTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.StatementNonOpinionTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a general statement.
 * This will directly call for either a nonopinionated statement or an opinionated one, so this
 * class should only be used when you don't know which to pick.
 */
public class StatementTemplate extends SemanticResponseTemplate {

    @Override
    public String constructDumbResponse(Conversation convo, KBController kb) {
        Random rand = new Random();
        if(rand.nextBoolean()) {
            return new StatementOpinionTemplate().constructDumbResponse(convo, kb);
        } else {
            return new StatementNonOpinionTemplate().constructDumbResponse(convo, kb);
        }
    }

}
