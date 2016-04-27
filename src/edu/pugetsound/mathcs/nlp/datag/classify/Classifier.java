package edu.pugetsound.mathcs.nlp.datag.classify;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

public interface Classifier {
	
	/**
	* Classifies an Utterance.
	**/
	public DialogueActTag classify(Utterance u, Conversation c);
}
