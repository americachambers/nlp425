package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jblas.DoubleMatrix;

import com.chilij.learning.neural.NeuralNetwork;
import com.chilij.learning.neural.NeuralNetwork.DimensionMismatchException;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Token;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

class ChiliNNClassifier implements Classifier {
	
	private final NeuralNetwork network;
	
	public ChiliNNClassifier(String filepath) throws FileNotFoundException {
		network = NeuralNetwork.readFrom(new Scanner(new File(filepath)));
	}

	@Override
	public DialogueActTag classify(Utterance u, Conversation c, TokenIndexMap tokenIndexMap) {
		
		double[][] inputVector = new double[1][tokenIndexMap.size()];
		
		for(Token token : u.tokens) {
			int tokenIndex = tokenIndexMap.indexForToken(token.token.toLowerCase());
			System.out.println("\t" + token.token + " : " + tokenIndex);
			if(tokenIndex != -1)
				inputVector[0][tokenIndex] = 1;
		}
		
		for(int i = 0; i < inputVector[0].length; i++)
			if(inputVector[0][i] != 0)
				System.out.println(i);
		
		DoubleMatrix outputVector = null;
		
		try {
			outputVector = this.network.forward(inputVector);
		} catch (DimensionMismatchException e) {
			System.err.println("Dimension mismatch in feed forward.");
			e.printStackTrace();
			System.exit(1);
		}
		
		int maxIndex = -1;
		double max = -1;
		
		for(int col = 0; col < outputVector.columns; col++) {
			System.out.printf("%.2f, ", outputVector.get(0, col));
			if(outputVector.get(0, col) > max) {
				maxIndex = col;
				max = outputVector.get(0, col);
			}
		}
		
		System.out.println();
		
		return DialogueActTag.values()[maxIndex];
	}

}
