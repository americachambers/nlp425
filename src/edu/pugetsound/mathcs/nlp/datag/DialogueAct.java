package edu.pugetsound.mathcs.nlp.datag;

import java.util.List;

class DialogueAct {
	
	private DialogueActTag tag;
	private List<String> words;
	
	public DialogueAct(String label, List<String> words) throws IllegalArgumentException {
		this.tag = DialogueActTag.fromLabel(label);
		this.words = words;
	}
	
	public DialogueActTag getTag() { return this.tag; }
	public List<String> getWords() { return this.words; }
	
}
