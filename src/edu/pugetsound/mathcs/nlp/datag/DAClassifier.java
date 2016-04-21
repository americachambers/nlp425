package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

public class DAClassifier {
	
	private static final String USER_DIR = "user.dir";
	private static final String BASE_DIR = "nlp425";
	private static final String ROOT = System.getProperty(USER_DIR).substring(0, System.getProperty(USER_DIR).indexOf(BASE_DIR) + BASE_DIR.length());
	private static final String INDEX_MAP_FILE = ROOT + "/models/datag/Token-Index-Map.txt";
	
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
