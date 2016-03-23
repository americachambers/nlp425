package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.util.List;

public class ClassifierTrainer {
	private List<File> files;
	private List<DialogueAct> tags;

	public ClassifierTrainer(List<String> paths) {
		//fill tags w/ parser
	}

	public void train(Classifier c) {
		c.train(tags);
	}
}