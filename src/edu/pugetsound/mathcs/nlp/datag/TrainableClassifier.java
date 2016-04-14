package edu.pugetsound.mathcs.nlp.datag;

import java.io.Serializable;
import java.util.List;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

interface Trainable extends Serializable {

	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DialogueAct> list, TokenIndexMap tokenIndexMap);
	
}

interface Classifier {
	/**
	* Classifies an Utterance.
	**/
	public DialogueActTag classify(Utterance u, Conversation c, TokenIndexMap tokenIndexMap);
}

interface TrainableClassifier extends Trainable, Classifier { }
