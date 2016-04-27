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

	private DialogueActTag tag;
	private List<String> words;

	/**
	 * Constructs a DialogueAct
	 * 
	 * @param tag
	 *            The DialogueActTag associated with the utterance
	 * @param words
	 *            A list of tokens from the utterance
	 */
	public DialogueAct(DialogueActTag tag, List<String> words) {
		this.tag = tag;
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
	 * Accessor for token list
	 * 
	 * @return The List of words
	 */
	public List<String> getWords() {
		return this.words;
	}

}
