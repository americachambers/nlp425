package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;

public interface SemanticResponseTemplate {
	
    public AMR constructAmrFromTemplate(Utterance utterance, DialogueActTag daTag);
        
}
