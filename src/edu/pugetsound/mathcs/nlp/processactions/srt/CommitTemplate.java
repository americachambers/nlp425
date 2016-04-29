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
 * A template for constructing a response which commits to an action, typically in response to a
 * user directive.
 * In the corpus, this almost always showed up as "I'll try to remember that" or
 * "I'll look into that".
 * This class should do some checking to see what kind of response should be returned given the
 * directive.
 */
public class CommitTemplate implements SemanticResponseTemplate {

    // The CommitTemplate is like "I'll try to remember that", "I'll look into that", etc.
    // Hardcoding might be ok, but we might also need to look at what they're saying to pick
    // between outputs

    private HashMap<AMR, String[]> outputs =
        SemanticResponseTemplate.responses.get(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));

    @Override
    public String constructResponseFromTemplate(Conversation convo) {
        Random rand = new Random();
        Utterance utterance = convo.getLastUtterance();
        AMR amr = (AMR) outputs.keySet().toArray()[rand.nextInt(outputs.size())];
        return amr.convertAMRToText(outputs.get(amr));
    }

}
