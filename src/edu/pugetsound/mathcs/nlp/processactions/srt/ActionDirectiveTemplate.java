package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

public class ActionDirectiveTemplate implements SemanticResponseTemplate {

    // The action directive tag corresponds to anytime you tell the other person to do something
    // It's typically in the corpus when people say "Go ahead" or "No, you go first".
    // It's also used for changing the subject, such as "Let's talk about X".
    // How about we use something like "Go ahead" when its appropriate, and try to change the subject otherwise
    
    private static final String[] outputs = {
        "Go ahead."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}

