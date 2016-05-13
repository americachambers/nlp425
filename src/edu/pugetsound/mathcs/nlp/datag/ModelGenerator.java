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
import edu.pugetsound.mathcs.nlp.util.Logger;
import edu.pugetsound.mathcs.nlp.util.PathFormat;

/**
 * This class just houses a main method which is used to generate
 * all of the models within models/datag/ which are loaded into
 * DAClassifier at runtime.
 * 
 * @author Creavesjohnson
 * @version 05/13/2016
 */
class ModelGenerator {

	// The .mallet file on which to train the classifiers
	private static final String INPUT_PATH = PathFormat
			.absolutePathFromRoot("models/datag/switchboard.mallet");

	// Save classifiers at the locations which will be read in DAClassifier
	private static final String NAIVE_BAYES_PATH = DAClassifier.NAIVE_BAYES_PATH;
	private static final String MAX_ENT_PATH = DAClassifier.MAX_ENT_PATH;
	private static final String DECISION_TREE_PATH = DAClassifier.DECISION_TREE_PATH;

	/**
	 * Generates / trains the models used in DAClassifier and saves them to
	 * disk.
	 */
	public static void generateModels() {
		File switchboardFile = new File(INPUT_PATH);
		InstanceList trainList = InstanceList.load(switchboardFile);

		// Used to calculate training times
		long startTime;
		long endTime;

		// Naive Bayes training
		if (Logger.debug()) {
			System.out.println("[DATAG] Training naive Bayes classifier...");
		}

		startTime = System.currentTimeMillis();
		saveNaiveBayes(NAIVE_BAYES_PATH, trainList);
		endTime = System.currentTimeMillis();

		if (Logger.debug()) {
			System.out
					.printf("[DATAG] Elapsed: %.2f seconds\n\n", ((endTime - startTime) / 1000.0));
		}

		// Maximum entropy training
		if (Logger.debug()) {
			System.out.println("[DATAG] Training maximum entropy classifier...");
		}

		startTime = System.currentTimeMillis();
		saveMaxEnt(MAX_ENT_PATH, trainList);
		endTime = System.currentTimeMillis();

		if (Logger.debug()) {
			System.out
					.printf("[DATAG] Elapsed: %.2f seconds\n\n", ((endTime - startTime) / 1000.0));
		}

		// Decision tree training
		if (Logger.debug()) {
			System.out.println("[DATAG] Training decision tree classifier...");
		}

		startTime = System.currentTimeMillis();
		saveDecisionTree(DECISION_TREE_PATH, trainList);
		endTime = System.currentTimeMillis();

		if (Logger.debug()) {
			System.out.printf("[DATAG] Elapsed: %.2f seconds\n", ((endTime - startTime) / 1000.0));
		}
	}

	/**
	 * Trains and saves a naive Bayes classifier
	 * 
	 * @param path
	 *            Path at which to save the classifier
	 * @param trainList
	 *            The list of instances on which to train
	 */
	private static void saveNaiveBayes(String path, InstanceList trainList) {
		ClassifierTrainer<NaiveBayes> trainer = new NaiveBayesTrainer();
		cc.mallet.classify.Classifier classifier = trainer.train(trainList);
		writeClassifier(path, classifier);
	}

	/**
	 * Trains and saves a maximum entropy classifier
	 * 
	 * @param path
	 *            Path at which to save the classifier
	 * @param trainList
	 *            The list of instances on which to train
	 */
	private static void saveMaxEnt(String path, InstanceList trainList) {
		ClassifierTrainer<MaxEnt> trainer = new MaxEntTrainer();
		cc.mallet.classify.Classifier classifier = trainer.train(trainList);
		writeClassifier(path, classifier);
	}

	/**
	 * Trains and saves a decision tree classifier
	 * 
	 * @param path
	 *            Path at which to save the classifier
	 * @param trainList
	 *            The list of instances on which to train
	 */
	private static void saveDecisionTree(String path, InstanceList trainList) {
		ClassifierTrainer<DecisionTree> trainer = new DecisionTreeTrainer();
		cc.mallet.classify.Classifier classifier = trainer.train(trainList);
		writeClassifier(path, classifier);
	}

	/**
	 * Serializes a classifier and writes it to disk
	 * 
	 * @param path
	 *            The path at which to save the classifier
	 * @param classifier
	 *            The classifier to save
	 */
	private static void writeClassifier(String path, cc.mallet.classify.Classifier classifier) {
		try {
			FileOutputStream fileStream = new FileOutputStream(path);
			ObjectOutputStream output = new ObjectOutputStream(fileStream);
			output.writeObject(classifier);
			output.close();

			if (Logger.debug()) {
				System.out.println("[DATAG] Saved " + path);
			}
		} catch (Exception e) {
			System.err.println("[DATAG] Could not save " + path + " to disk.\n" + e.toString());
		}
	}

	/**
	 * Generates and saves NaiveBayes, DecisionTree, and MaxEnt classifiers into
	 * the models/datag directory.
	 * 
	 * @param args
	 *            Unused
	 */
	public static void main(String[] args) {
		generateModels();
	}

}
