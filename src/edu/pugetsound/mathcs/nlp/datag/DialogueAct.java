package edu.pugetsound.mathcs.nlp.datag;

import java.util.List;

/**
 * This object encapsulates a single dialogue act, including a DialogueActTag
 * and a list of tokens
 * 
 * @author Creavesjohnson
 *
 */
class DialogueAct {

	private final DialogueActTag tag;
	private final DialogueActTag previousTag;
	private final List<String> words;

	/**
	 * Constructs a DialogueAct
	 * 
	 * @param tag
	 *            The DialogueActTag associated with the utterance
	 * @param previousTag
	 * 			  The DialogueActTag from the previous utterance
	 * @param words
	 *            A list of tokens from the utterance
	 */
	public DialogueAct(DialogueActTag tag, DialogueActTag previousTag, List<String> words) {
		this.tag = tag;
		this.previousTag = previousTag;
		this.words = words;
	}

	/**
	 * Appends a word to the token list.
	 * This is used in resolving CONTINUED_FROM_PREVIOUS tags in Switchboard
	 * 
	 * @param words
	 */
	public void appendWords(List<String> words) {
		this.words.addAll(words);
	}

	/**
	 * Accessor for tag
	 * 
	 * @return The DialgueActTag
	 */
	public DialogueActTag getTag() {
		return this.tag;
	}
	
	/**
	 * Accessor for previousTag
	 * @return The DialogueActTag of the previous utterance
	 */
	public DialogueActTag getPreviousTag() {
		return this.previousTag;
	}

	/**
	 * Accessor for token list
	 * 
	 * @return The List of words
	 */
	public List<String> getWords() {
		return this.words;
	}

}
