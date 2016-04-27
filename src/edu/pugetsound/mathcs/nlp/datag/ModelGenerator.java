package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.DecisionTree;
import cc.mallet.classify.DecisionTreeTrainer;
import cc.mallet.classify.MaxEnt;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.types.InstanceList;
import edu.pugetsound.mathcs.nlp.util.PathFormat;


/**
 * This class just houses a main method which is used to generate
 * all of the models within models/datag/ which are loaded into
 * DAClassifier at runtime.
 */
class ModelGenerator {
	
	private static final String INPUT_PATH = PathFormat.absolutePathFromRoot("models/datag/switchboard.mallet");
	private static final String NAIVE_BAYES_PATH = DAClassifier.NAIVE_BAYES_PATH;
	private static final String MAX_ENT_PATH = DAClassifier.MAX_ENT_PATH;
	private static final String DECISION_TREE_PATH = DAClassifier.DECISION_TREE_PATH;

	public static void main(String[] args) {

		File switchboardFile = new File(INPUT_PATH);
		InstanceList trainList = InstanceList.load(switchboardFile);
		
		long startTime;
		long endTime;
		
		// Naive Bayes training
		System.out.println("Training naive Bayes classifier...");
		
		startTime = System.currentTimeMillis();
		saveNaiveBayes(NAIVE_BAYES_PATH, trainList);
		endTime = System.currentTimeMillis();
		
		System.out.printf("Elapsed: %.2f seconds\n\n", ((endTime - startTime)/1000.0));
		
		// Maximum entropy training
		System.out.println("Training maximum entropy classifier...");
		
		startTime = System.currentTimeMillis();
		saveMaxEnt(MAX_ENT_PATH, trainList);
		endTime = System.currentTimeMillis();
		
		System.out.printf("Elapsed: %.2f seconds\n\n", ((endTime - startTime)/1000.0));
		
		// Decision tree training
		System.out.println("Training decision tree classifier...");
		
		startTime = System.currentTimeMillis();
		saveDecisionTree(DECISION_TREE_PATH, trainList);
		endTime = System.currentTimeMillis();
		
		System.out.printf("Elapsed: %.2f seconds\n", ((endTime - startTime)/1000.0));
		
	}
	
	private static void saveNaiveBayes(String path, InstanceList trainList) {
		ClassifierTrainer<NaiveBayes> trainer = new NaiveBayesTrainer();
		cc.mallet.classify.Classifier classifier = trainer.train(trainList);
		writeClassifier(path, classifier);	
	}
	
	private static void saveMaxEnt(String path, InstanceList trainList) {
		ClassifierTrainer<MaxEnt> trainer = new MaxEntTrainer();
		cc.mallet.classify.Classifier classifier = trainer.train(trainList);
		writeClassifier(path, classifier);
	}
	
	private static void saveDecisionTree(String path, InstanceList trainList) {
		ClassifierTrainer<DecisionTree> trainer = new DecisionTreeTrainer();
		cc.mallet.classify.Classifier classifier = trainer.train(trainList);
		writeClassifier(path, classifier);
	}
	
	private static void writeClassifier(String path, cc.mallet.classify.Classifier classifier) {
		try {
			FileOutputStream fileStream = new FileOutputStream(path);
			ObjectOutputStream output = new ObjectOutputStream(fileStream);
			output.writeObject(classifier);
			output.close();
			System.out.println("Saved " + path);
		} catch (Exception e) {
			System.err.println("Could not save " + path + " to disk.\n" + e.toString());
		}
	}

}
