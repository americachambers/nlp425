package edu.pugetsound.mathcs.nlp.datag;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Token;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import org.neuroph.core.*;
import org.neuroph.core.learning.*;
import org.neuroph.core.data.*;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.nnet.learning.SigmoidDeltaRule;
import org.neuroph.nnet.learning.SupervisedHebbianLearning;
import org.neuroph.util.TransferFunctionType; 


/**
 * Neural Net Classifier. Currently under construction.
 * @author Lukas
 */
public class NeuralNetClassifier implements TrainableClassifier {

	NeuralNetwork<SupervisedHebbianLearning> nn;
	String filePath;
	
	/**
	 * Constructor. Takes a token index map and instantiates the neural network.
	 * @param t the token index map.
	 */
	public NeuralNetClassifier(TokenIndexMap t){
		this.filePath = "nn.nnet";
		nn = new Perceptron(t.size()+1, DialogueActTag.values().length,TransferFunctionType.SIGMOID);
		SupervisedHebbianLearning sdr = new SupervisedHebbianLearning();
		nn.setLearningRule(sdr);
	}
	/**
	 * Constructor for pre-built networks. Loads the network from a file.
	 * @param filePath the path of the file.
	 */
	public NeuralNetClassifier(String filePath){
		this.filePath = filePath;
		nn = load(filePath);
	}
	
	@Override
	/**
	 * Classifies an utterance. 
	 * @param u the utterance
	 * @param c the conversation
	 * @param t the token index map
	 * @return the dialogue act tag.
	 */
	public DialogueActTag classify(Utterance u, Conversation c, TokenIndexMap t) {
		double[] input = new double[u.tokens.size()];
		List<String> words = new ArrayList<>(u.tokens.size());
		
		for(int i = 0; i < u.tokens.size(); i++){
			words.add(i, u.tokens.get(i).token);
		}
		input = encode(words,t);
		nn.setInput(input);
		nn.calculate();
		double[] output = nn.getOutput();
		double tagVal = 0;
		int tagIndex = -1;
		for(int i = 0; i < output.length; i++){
			if (tagVal < output[i]){
				tagIndex = i;
				tagVal = output[i];
			}
			System.out.println("[DATAG] " + nn.getOutput()[i]);
		}
		return DialogueActTag.values()[tagIndex];
	}
	
	@Override
	/**
	 * Trains network, given a dialogue act list and token index map.
	 * @param list the dialogue act list
	 * @param t the token index map
	 */
	public void train(List<DialogueAct> list, TokenIndexMap t) {
		System.out.println("# words: " + t.size());

		System.out.println("Initiated NN. " + list.size());
//		DataSet dataSet = new DataSet(t.size(),DialogueActTag.values().length);
		long time = System.currentTimeMillis();
		for (int block = 0; block < list.size(); block+=50){
			DataSet dataSet = new DataSet(t.size()+1,DialogueActTag.values().length);
			for(int i = 0; (i < 50) && ((block + i) < list.size()); i++){
				DialogueAct da = list.get(block+i);
				double[] inputVector = encode(da.getWords(),t);
				double[] outputVector = new double[DialogueActTag.values().length];
				outputVector[da.getTag().ordinal()] = 1.0;
				dataSet.addRow(inputVector, outputVector);
			}
			nn.learn(dataSet);
		}
				
		System.out.println((System.currentTimeMillis()-time) + " ms training time." );
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		nn.save(filePath);
	}
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		nn = NeuralNetwork.createFromFile(filePath);
	}
	
	public void save(String filePath){
		nn.save(filePath);
	}
	
	public static NeuralNetwork load(String filePath){
		return NeuralNetwork.createFromFile(filePath);
	}
	
	// encodes a list of words into a boolean array, 
	// where each word corresponds to an index in the token index map
	private double[] encode(List<String> words, TokenIndexMap t){
		double[] inputVector = new double[t.size()+1];
		
		for (int i = 0; i< words.size(); i++){
			int index = t.indexForToken(words.get(i));
			if (index >= 0) inputVector[index] = 1.0;	//one-hot encoding
		}
		inputVector[t.size()] = 1;
		return inputVector;
	}
}
