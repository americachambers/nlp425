package edu.pugetsound.mathcs.nlp.datag;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

public class DAClassifier {
	
	private static final String SWITCHBOARD_DIR = "resources/swb1_dialogact_annot";

	public DAClassifier() {
		
	}
	
	public DialogueActTag classify(Utterance utterance, Conversation conversation) {
		int randIndex = (int)(Math.random() * DialogueActTag.values().length);
		return DialogueActTag.values()[randIndex];
	}
	
}
