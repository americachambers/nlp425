package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.YesTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.NoTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.IndeterminateResponseTemplate;

public class YesNoAnswerTemplate implements SemanticResponseTemplate {

    @Override
    public String constructResponseFromTemplate(Utterance utterance) {
        Random rand = new Random();
        switch(rand.nextInt(2)) {
        case 0:
            return new YesTemplate().constructResponseFromTemplate(utterance);
        case 1:
            return new NoTemplate().constructResponseFromTemplate(utterance);
        }

        // Else
        return new IndeterminateResponseTemplate().constructResponseFromTemplate(utterance);
    }

}

