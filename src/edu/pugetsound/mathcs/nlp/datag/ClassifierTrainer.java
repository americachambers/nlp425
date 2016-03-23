package edu.pugetsound.mathcs.nlp.datag;

public class ClassifierTrainer {
	List<File> files;
	List<DATag> tags;

	public ClassifierTrainer(String path){
		//fill tags w/ parser
	}

	public void train(Classifier c){
		c.train(tags);
	}
}