package edu.pugetsound.mathcs.nlp.datag;

import java.util.List;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

interface Classifier {
	/**
	* Classifies an Utterance.
	**/
	public DialogueActTag classify(Utterance u);

	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DialogueAct> list);
}