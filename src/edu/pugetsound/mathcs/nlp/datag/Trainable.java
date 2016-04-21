package edu.pugetsound.mathcs.nlp.datag;

import java.io.Serializable;
import java.util.List;

interface Trainable extends Serializable {

	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DialogueAct> list, TokenIndexMap tokenIndexMap);
	
}
