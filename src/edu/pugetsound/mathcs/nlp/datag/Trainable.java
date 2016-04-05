package edu.pugetsound.mathcs.nlp.datag;

import java.util.List;

interface Trainable {

	/**
	* 	Trains claffier based on a previous
	**/
	public void train(List<DialogueAct> list);
	
}
