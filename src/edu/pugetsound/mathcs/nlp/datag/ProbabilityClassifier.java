package edu.pugetsound.mathcs.nlp.datag;

import edu.pugetsound.mathcs.nlp.lang.Utterance;


class ProbabilityClassifier implements Classifier {
	
	List<DialogueAct> list;

	public DialogueAct classify(Utterance u){
		int index = (int)Math.random()*list.size();
		DialogueAct da = list.get(index);
		return new DialgueAct(da.getTag(),u.tokens);
	} 


	public void train(List<DialogueAct> list){
		this.list = list;
	}

	public static void main(String[] args){
		Utterance u = new Utterance(args[0]);
		ProbabilityClassifier c = new ProbabilityClassifier();

		c.train(null);
		DialogueAct da = c.classify(u);
		System.out.println(da.getTag());
	}
}