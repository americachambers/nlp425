package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;

public interface SemanticResponseTemplate {

    public String constructResponseFromTemplate(Utterance utterance);
        
}

