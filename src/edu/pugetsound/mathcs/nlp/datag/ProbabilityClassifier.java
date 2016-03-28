package edu.pugetsound.mathcs.nlp.datag;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;


class ProbabilityClassifier implements Classifier {
	
	List<DialogueAct> list;

	public DialogueActTag classify(Utterance u){
		int index = (int)(Math.random()*list.size());
		DialogueAct da = list.get(index);
		return da.getTag();
	} 


	public void train(List<DialogueAct> list){
		this.list = list;
	}

	public static void main(String[] args){
		Utterance u = new Utterance("");
		ProbabilityClassifier c = new ProbabilityClassifier();
		File file = new File(args[0]);
		SwitchboardParser sp;
		try {
			sp = new SwitchboardParser(file);
			c.train(sp.getActs(DialogueActTag.class.getEnumConstants()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

		DialogueActTag dat = c.classify(u);
		System.out.println("[DATAG] Probability Classification: " + dat);
	}
}