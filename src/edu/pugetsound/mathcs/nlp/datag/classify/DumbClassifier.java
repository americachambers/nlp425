package edu.pugetsound.mathcs.nlp.datag.classify;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

/**
 * Dumb classifier which contains only hardcoded rules to detect specific types
 * of questions.
 * 
 * @author Creavesjohnson
 * @version 05/13/2016
 */
public class DumbClassifier implements Classifier {

	// Prefixes which signal a QUESTION_YES_NO
	private static final String[] YES_NO_PREFIX = { "is", "isn't", "are", "aren't", "do", "don't",
			"did", "didn't", "does", "doesn't", "can", "can't", "would", "wouldn't", "should",
			"shouldn't", "shall", "shan't" };
	// WH- words
	private static final String[] WH_PREFIX = { "who", "what", "where", "when", "why", "how" };

	private static final String[] IS_ARE = { "is", "are" };
	private static final String OR = " or ";

	/**
	 * Classifies an utterance into its question type based on hard-coded rules.
	 * 
	 * @param u
	 *            The utterance to classify
	 * @param c
	 *            The conversation containing the utterance in question
	 * @return The predicted DialogueActTag or null if it could not be
	 *         classified
	 */
	public DialogueActTag classify(Utterance u, Conversation c) {
		String utterance = u.utterance;

		if (utterance.contains("?")) {
			if (!startsWithAny(utterance, IS_ARE) && utterance.contains(OR)) {
				return DialogueActTag.QUESTION_ALTERNATIVE;
			} else if (startsWithAny(utterance, YES_NO_PREFIX)) {
				if (utterance.contains(" or "))
					return DialogueActTag.QUESTION_YES_NO_OR;
				else
					return DialogueActTag.QUESTION_YES_NO;
			} else if (startsWithAny(utterance, WH_PREFIX)) {
				return DialogueActTag.QUESTION_WH;
			} else {
				return randomTag(DialogueActTag.QUESTION, DialogueActTag.QUESTION_ALTERNATIVE,
						DialogueActTag.QUESTION_OPEN_ENDED, DialogueActTag.QUESTION_RHETORICAL);
			}
		} else {
			return null;
		}
	}

	/**
	 * Determines whether or not a string starts with ANY of the given prefixes
	 * 
	 * @param string
	 *            The string in question
	 * @param prefixes
	 *            An array of prefixes to check against the string
	 * @return true if the string starts with any of the prefixes, false
	 *         otherwise
	 */
	private boolean startsWithAny(String string, String... prefixes) {
		for (String prefix : prefixes)
			if (string.toLowerCase().startsWith(prefix))
				return true;
		return false;
	}

	/**
	 * Selects a random DialogueActTag out of an array of DialogueActTags
	 * 
	 * @param tags
	 *            An array of DialogueActTags
	 * @return A random element from the array, null if the array is empty
	 */
	private DialogueActTag randomTag(DialogueActTag... tags) {
		if(tags.length > 0) {
			int randIndex = (int) (Math.random() * tags.length);
			return tags[randIndex];
		}
		return null;
	}

}