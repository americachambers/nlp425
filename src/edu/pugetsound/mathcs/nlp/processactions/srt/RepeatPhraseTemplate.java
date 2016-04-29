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
 * A template for constructing a response which isolates a noun phrase in the user's input,
 * to signal incredularity or interest.
 * For example, the user says "We ate the whole cat", and so we might respond with "The whole cat."
 */
public class RepeatPhraseTemplate implements SemanticResponseTemplate {

    // The repeat phrase template takes the subject focus of what the user just said, and says just
    //that
    // For example, user says: "We were there for three months".
    // You'd output "Three months."
    // We need to make sure to only output named entities though, so we don't say "There."

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
