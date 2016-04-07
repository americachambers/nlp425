package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;

/**
 * @author Thomas Gagne
 * The interface for all response templates.
 * All templates need to be able to take in an utterance and return a String response.
 */
public interface SemanticResponseTemplate {

    /**
     * @author Thomas Gagne
     * Returns a response to a user's utterance in string form.
     * Since interfaces in Java can't have static methods, this method must be called on an object.
     * For inline expressions, new MyTemplate().constructResponseFromTemplate(utt); works well
     *
     * @param utterance The utteranec corresponding to the user's input.
     * @return A string response. In early versions, this might be an AMR response.
     */
    public String constructResponseFromTemplate(Utterance utterance);
        
}

