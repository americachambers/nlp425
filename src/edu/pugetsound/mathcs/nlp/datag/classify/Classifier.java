package edu.pugetsound.mathcs.nlp.datag.classify;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

/**
 * Interface for classifiers which insists that they must be able to classifier
 * an utterance, possibly in the context of a conversation
 * 
 * @author Creavesjohnson
 * @version 05/13/2016
 */
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
