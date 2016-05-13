package edu.pugetsound.mathcs.nlp.datag;

import edu.pugetsound.mathcs.nlp.datag.classify.Classifier;
import edu.pugetsound.mathcs.nlp.datag.classify.DumbClassifier;
import edu.pugetsound.mathcs.nlp.datag.classify.MalletClassifier;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.util.PathFormat;

/**
 * This is an object which is responsible for classifying utterances with
 * DialogueActTags
 * 
 * @author Creavesjohnson
 * @version 05/13/2016
 */
public class DAClassifier {

	// Paths to trained classifiers
	static final String NAIVE_BAYES_PATH = PathFormat
			.absolutePathFromRoot("models/datag/naive-bayes.classifier");
	static final String MAX_ENT_PATH = PathFormat
			.absolutePathFromRoot("models/datag/max-ent.classifier");
	static final String DECISION_TREE_PATH = PathFormat
			.absolutePathFromRoot("models/datag/decision-tree.classifier");

	private final Classifier dumbClassifier;
	private final Mode mode;

	private Classifier secondaryClassifier;

	/**
	 * Classifier modes for the DAClassifier.
	 * DUMB_ modes hierarchically consult the the DumbClassifier before falling
	 * back to the specified classifier.
	 * Classifiers not prefixed by DUMB_ bypass the DumbClassifier.
	 * 
	 * @author Creavesjohnson
	 *
	 */
	public enum Mode {
		NAIVE_BAYES(false), MAX_ENT(false), DECISION_TREE(false), DUMB_NAIVE_BAYES(true), DUMB_MAX_ENT(
				true), DUMB_DECISION_TREE(true);

		private boolean isDumb;

		Mode(boolean isDumb) {
			this.isDumb = isDumb;
		}

	}

	/**
	 * Constructs a new DAClassifier
	 * This constructor loads and parses the Switchboard data set
	 */
	public DAClassifier(Mode mode) {

		this.mode = mode;

		if (this.mode.isDumb) {
			dumbClassifier = new DumbClassifier();
		} else {
			dumbClassifier = null;
		}

		final String path;

		// Load classifier path based on mode
		switch (this.mode) {
			case NAIVE_BAYES:
			case DUMB_NAIVE_BAYES:
				path = NAIVE_BAYES_PATH;
				break;

			case MAX_ENT:
			case DUMB_MAX_ENT:
				path = MAX_ENT_PATH;
				break;

			case DECISION_TREE:
			case DUMB_DECISION_TREE:
				path = DECISION_TREE_PATH;
				break;

			// Default to the naive Bayes classifier
			default:
				path = NAIVE_BAYES_PATH;
		}

		try {
			secondaryClassifier = new MalletClassifier(path);
		} catch (Exception e) {
			System.err.printf("Could not load Mallet classifier: %s\n", path);
			System.err.println(e.toString());
		}

	}

	/**
	 * Predicts the type of dialogue act of an utterance
	 * 
	 * @param utterance
	 *            An utterance
	 * @param conversation
	 *            The conversation in which the utterance appears
	 * @return The predicted DialogueActTag for the utterance
	 */
	public DialogueActTag classify(Utterance utterance, Conversation conversation) {
		if (dumbClassifier != null) {

			DialogueActTag tag = dumbClassifier.classify(utterance, conversation);

			if (tag != null) {
				return tag;
			} else {
				return secondaryClassifier.classify(utterance, conversation);
			}

		} else {
			return secondaryClassifier.classify(utterance, conversation);
		}
	}

}
