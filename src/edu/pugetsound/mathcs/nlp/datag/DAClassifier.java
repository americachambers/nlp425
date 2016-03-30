package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

public class DAClassifier {
	
	private static final String SWITCHBOARD_DIR = "resources/swb1_dialogact_annot";
	
	private final Classifier dumbClassifier;
	
	/**
	 * Constructs a new DAClassifier
	 * This constructor loads and parses the Switchboard data set
	 */
	public DAClassifier() {
		File switchboardData = new File(SWITCHBOARD_DIR);
		SwitchboardParser parser = null;
		
		try {
			parser = new SwitchboardParser(switchboardData);
		} catch (FileNotFoundException e) {
			System.err.println("[DATAG] Could not load Switchboard data from " + switchboardData.getAbsolutePath() + " : File not found.");
		}
		
		dumbClassifier = new ProbabilityClassifier();
		dumbClassifier.train(parser.getActs());
		
	}
	
	/**
	 * Predicts the type of dialogue act of an utterance
	 * @param utterance An utterance
	 * @param conversation The conversation in which the utterance appears
	 * @return The predicted DialogueActTag for the utterance
	 */
	public DialogueActTag classify(Utterance utterance, Conversation conversation) {
		return dumbClassifier.classify(utterance);
	}
	
}
