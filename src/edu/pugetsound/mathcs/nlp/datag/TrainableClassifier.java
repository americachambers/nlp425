package edu.pugetsound.mathcs.nlp.datag;

import java.util.List;

import edu.pugetsound.mathcs.nlp.lang.Utterance;

interface Trainable {

	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DialogueAct> list);
	
}

interface Classifier {
	/**
	* Classifies an Utterance.
	**/
	abstract public DialogueActTag classify(Utterance u);
}

interface TrainableClassifier extends Trainable, Classifier { }
