package edu.pugetsound.mathcs.nlp.processactions.srt;

import java.util.Random;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;
import edu.pugetsound.mathcs.nlp.processactions.srt.StatementTemplate;

public class CommentTemplate implements SemanticResponseTemplate {

    @Override
    public static String constructResponseFromTemplate(Utterance utterance) {
        return StatementTemplate.constructResponseFromTemplate(utterance);
    }

}
