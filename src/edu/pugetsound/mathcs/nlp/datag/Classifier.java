package edu.pugetsound.mathcs.nlp.datag;

import java.util.List;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

abstract class Classifier {
	/**
	* Classifies an Utterance.
	**/
	abstract public DialogueActTag classify(Utterance u);

	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DialogueAct> list) { }
}