package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a response which greets the user.
 * Example responses include "How's it going?" or "What's up?"
 * Note that this does not say "Hi", which is reserved for GreetingTemplate
 */
public class ConventionalOpeningTemplate implements SemanticResponseTemplate {

    // Conventional opening can also involve saying your name
    // We might want to add that.

    private static final HashMap<String, AMR> outputs = SemanticResponseTemplate.getResponses("ConventionalOpeningTemplate");


    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return ((String) outputs.keySet().toArray()[rand.nextInt(outputs.size())]);
    }

}

