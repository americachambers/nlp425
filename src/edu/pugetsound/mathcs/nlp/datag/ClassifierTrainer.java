package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class just houses a main method which is used to generate
 * all of the models within models/datag/ which are loaded into
 * DAClassifier at runtime.
 */
class ClassifierTrainer {

	public static void main(String[] args) {
		// TODO Put all of the training / model saving in here!
		String dir0 = "resources/swb1_dialogact_annot/sw00utt";
		String dir1 = "resources/swb1_dialogact_annot/sw01utt";
		String dir2 = "resources/swb1_dialogact_annot/sw02utt";

		File dir = new File("resources/swb1_dialogact_annot/scrubbed");
		File[] subDirs = {new File(dir0),new File(dir1),new File(dir2)};
		SwitchboardParser wholeParser = null;
		try {
			wholeParser = new SwitchboardParser(dir);
		} catch (FileNotFoundException e) {
			System.err.println("[DATAG] Could not load Switchboard data from " + dir.getAbsolutePath() + " : File not found.");
		}
		
		TokenIndexMap tim = wholeParser.getTokenIndexMap();
		NeuralNetClassifier nnC;
		nnC = new NeuralNetClassifier(tim);		
		

		System.out.println("Training...");
		nnC.train(wholeParser.getActs(),tim);
		
		nnC.save("models/datag/nn.nnet");

	}
}
