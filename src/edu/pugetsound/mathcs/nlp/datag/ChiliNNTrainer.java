package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.chilij.learning.neural.BackPropNeuralNetwork;
import com.chilij.learning.neural.NeuralNetwork;
import com.chilij.learning.neural.NeuralNetwork.DimensionMismatchException;
import com.chilij.learning.neural.Topology;

public class ChiliNNTrainer {
	
	private static final String SWITCHBOARD_DIR = "resources/swb1_dialogact_annot/scrubbed";
	private static final int CHUNK_SIZE = 500;
	private static final String OUTPUT_FILE = "models/datag/chili-nn-test.nn";
	private static final String TOKEN_INDEX_FILE = "models/datag/Token-Index-Map.txt";

	public static void main(String[] args) {
		
		SwitchboardParser parser = null;
		File switchboardData = new File(SWITCHBOARD_DIR);
		
		try {
			parser = new SwitchboardParser(switchboardData);
		} catch (FileNotFoundException e) {
			System.err.println("[DATAG] Could not load Switchboard data from " + switchboardData.getAbsolutePath() + " : File not found.");
			System.exit(1);
		}
		
		TokenIndexMap indexMap = parser.getTokenIndexMap();
		int numUniqueTokens = indexMap.size();
		int numTags = DialogueActTag.values().length;
		
		try {
			indexMap.saveToFile(TOKEN_INDEX_FILE);
		} catch (FileNotFoundException e) {
			System.err.println("[DATAG] Could not save " + TOKEN_INDEX_FILE);
		}
		
		Topology top = new Topology(numUniqueTokens, 1, 5, numTags);
		BackPropNeuralNetwork network = new BackPropNeuralNetwork(0.3, top);
		
		List<DialogueAct> acts = parser.getActs();
		System.out.println(acts.size() + " : " + numUniqueTokens);
		System.out.println(Integer.MAX_VALUE / (double)(numUniqueTokens));
		
		for(int a = 0; a < acts.size(); a += CHUNK_SIZE) {
			System.out.println("Training " + ((double)(a) / (double)(acts.size()) * 100) + "% complete...");
			
			double[][] trainingFeaturesArray = new double[CHUNK_SIZE][numUniqueTokens];
			double[][] trainingLabelsArray = new double[CHUNK_SIZE][numTags];
			
			int actualChunk = CHUNK_SIZE > acts.size() - a ? acts.size() - a : CHUNK_SIZE;
			
			for(int c = 0; c < actualChunk; c++) {
				int actIndex = a + c;
				DialogueAct act = acts.get(actIndex);
				
//				System.out.println(act.getTag() + " : " + DialogueActTag.indexOf(act.getTag()));
				trainingLabelsArray[c][DialogueActTag.indexOf(act.getTag())] = 1;
				
				for(String token : act.getWords()) {
					trainingFeaturesArray[c][indexMap.indexForToken(token.toLowerCase())] = 1;
				}
			}
			
			try {
				network.train(trainingFeaturesArray, trainingLabelsArray, 0.01);
			} catch (DimensionMismatchException e) {
				e.printStackTrace();
			}
		}
		
		try {
			PrintWriter out = new PrintWriter(OUTPUT_FILE);
			out.print(network);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
