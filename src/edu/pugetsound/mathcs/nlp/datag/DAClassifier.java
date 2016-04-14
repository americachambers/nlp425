package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

public class DAClassifier {
	
	private static final String INDEX_MAP_FILE = "models/datag/Token-Index-Map.txt";
	
	private Classifier dumbClassifier;
	private TokenIndexMap tokenIndexMap;
	
	/**
	 * Constructs a new DAClassifier
	 * This constructor loads and parses the Switchboard data set
	 */
	public DAClassifier() {
		
		try {
			dumbClassifier = new DumbClassifier();
			tokenIndexMap = new TokenIndexMap(new File(INDEX_MAP_FILE));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Predicts the type of dialogue act of an utterance
	 * @param utterance An utterance
	 * @param conversation The conversation in which the utterance appears
	 * @return The predicted DialogueActTag for the utterance
	 */
	public DialogueActTag classify(Utterance utterance, Conversation conversation) {
		return dumbClassifier.classify(utterance, conversation, tokenIndexMap);
	}
	
}
