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
 * A template for constructing a response which instructs the user to do something.
 * Almost every single time this appeared in the corpus, it was used to tell the user to
 * "Go ahead", as in to start speaking. Currently, this is all this template does, but it might
 * become more advanced.
 */
public class ActionDirectiveTemplate implements SemanticResponseTemplate {

    // The action directive tag corresponds to anytime you tell the other person to do something
    // It's typically in the corpus when people say "Go ahead" or "No, you go first".
    // It's also used for changing the subject, such as "Let's talk about X".
    // How about we use something like "Go ahead" when its appropriate, and try to change the
    // subject otherwise

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
