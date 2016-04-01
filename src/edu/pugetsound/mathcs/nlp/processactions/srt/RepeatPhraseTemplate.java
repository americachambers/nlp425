package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

public class RepeatPhraseTemplate implements SemanticResponseTemplate {

    // The repeat phrase template takes the subject focus of what the user just said, and says just that
    // For example, user says: "We were there for three months".
    // You'd output "Three months."
    // We need to make sure to only output named entities though, so we don't say "There."
    
    private static final String[] outputs = {
        "Right."
    };

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return outputs[rand.nextInt(outputs.length)];
    }

}

