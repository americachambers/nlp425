package edu.pugetsound.mathcs.nlp.datag;

import java.util.List;

import edu.pugetsound.mathcs.nlp.lang.Utterance;


class ProbabilityClassifier implements Classifier {
	
	List<DialogueAct> list;

	public DialogueActTag classify(Utterance u){
		int index = (int)Math.random()*list.size();
		DialogueAct da = list.get(index);
		return da.getTag();
	} 


	public void train(List<DialogueAct> list){
		this.list = list;
	}

}