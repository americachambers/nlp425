package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;
import java.util.HashMap;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne
 * A template for constructing a more positive or enthusiastic response to a rhetorical statement.
 * For example the user says "Well, you can't really eat a cat." and the response would be "Right?"
 */
public class TagQuestionTemplate implements SemanticResponseTemplate {

    private static final HashMap<String, AMR> outputs = SemanticResponseTemplate.getResponses("TagQuestionTemplate");
    
    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        return ((String) outputs.keySet().toArray()[rand.nextInt(outputs.size())]);
    }

}

