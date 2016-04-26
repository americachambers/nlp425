package edu.pugetsound.mathcs.nlp.datag;

import edu.pugetsound.mathcs.nlp.datag.classify.Classifier;
import edu.pugetsound.mathcs.nlp.datag.classify.DumbClassifier;
import edu.pugetsound.mathcs.nlp.datag.classify.MalletClassifier;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.util.PathFormat;

public class DAClassifier {
	
	static final String NAIVE_BAYES_PATH =
			PathFormat.absolutePathFromRoot("models/datag/naive-bayes.classifier");
	static final String MAX_ENT_PATH =
			PathFormat.absolutePathFromRoot("models/datag/max-ent.classifier");
	static final String DECISION_TREE_PATH =
			PathFormat.absolutePathFromRoot("models/datag/decision-tree.classifier");
	
	private final Classifier DUMB_CLASSIFIER;
	private final Mode MODE;
	
	private Classifier secondaryClassifier;
	
	public enum Mode {
		NAIVE_BAYES(false),
		MAX_ENT(false),
		DECISION_TREE(false),
		DUMB_NAIVE_BAYES(true),
		DUMB_MAX_ENT(true),
		DUMB_DECISION_TREE(true);
		
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
		
		this.MODE = mode;
		
		if(this.MODE.isDumb) {
			DUMB_CLASSIFIER = new DumbClassifier();
		} else {
			DUMB_CLASSIFIER = null;
		}
		
		final String PATH;
		
		switch(this.MODE) {
		case NAIVE_BAYES :
		case DUMB_NAIVE_BAYES :
			PATH = NAIVE_BAYES_PATH;
			break;
			
		case MAX_ENT :
		case DUMB_MAX_ENT :
			PATH = MAX_ENT_PATH;
			break;
			
		case DECISION_TREE :
		case DUMB_DECISION_TREE :
			PATH = DECISION_TREE_PATH;
			break;
			
		default :
			PATH = NAIVE_BAYES_PATH;
		}
		
		try {
			secondaryClassifier = new MalletClassifier(PATH);
		} catch(Exception e) {
			System.err.printf("Could not load Mallet classifier: %s\n", PATH);
			System.err.println(e.toString());
		}
		
	}
	
	/**
	 * Predicts the type of dialogue act of an utterance
	 * @param utterance An utterance
	 * @param conversation The conversation in which the utterance appears
	 * @return The predicted DialogueActTag for the utterance
	 */
	public DialogueActTag classify(Utterance utterance, Conversation conversation) {
		if(DUMB_CLASSIFIER != null) {
			
			DialogueActTag tag = DUMB_CLASSIFIER.classify(utterance, conversation);
			
			if(tag != null) {
				return tag;
			} else {
				return secondaryClassifier.classify(utterance, conversation);
			}

		} else {
			return secondaryClassifier.classify(utterance, conversation);
		}
	}
	
}
