package edu.pugetsound.mathcs.nlp.datag.classify;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

public interface Classifier {

	/**
	 * Classifies an Utterance in the context of a Conversation
	 * 
	 * @param utterance
	 *            An utterance
	 * @param conversation
	 *            The conversation in which the utterance appears
	 * @return The predicted DialogueActTag for the utterance
	 */
	public DialogueActTag classify(Utterance utterance, Conversation conversation);
}
