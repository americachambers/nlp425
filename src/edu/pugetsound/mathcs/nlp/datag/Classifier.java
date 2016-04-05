package edu.pugetsound.mathcs.nlp.datag;

import edu.pugetsound.mathcs.nlp.lang.Utterance;

interface Classifier {
	/**
	* Classifies an Utterance.
	**/
	abstract public DialogueActTag classify(Utterance u);
}