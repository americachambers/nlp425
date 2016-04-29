package edu.pugetsound.mathcs.nlp.datag.classify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

import cc.mallet.pipe.iterator.LineIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.Labeling;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Punctuation;
import edu.pugetsound.mathcs.nlp.lang.Token;
import edu.pugetsound.mathcs.nlp.lang.Utterance;

public class MalletClassifier implements Classifier {

	private final cc.mallet.classify.Classifier CLASSIFIER;
	
	public MalletClassifier(String filepath) throws
		FileNotFoundException, IOException, ClassNotFoundException {
		
		// Load the serialized classifier from the given path
		File inputFile = new File(filepath);
		FileInputStream fileStream = new FileInputStream(inputFile);
		ObjectInputStream objectStream = new ObjectInputStream(fileStream);
		
	    CLASSIFIER = (cc.mallet.classify.Classifier) objectStream.readObject();
	    
	    objectStream.close();

	}

	@Override
	public DialogueActTag classify(Utterance u, Conversation c) {
		
		String utterance = utteranceToString(u).toLowerCase();
		
		Reader inputReader = new StringReader(utterance);
		LineIterator input = new LineIterator(inputReader, "(.*)$", 1, 0, 0);
		Iterator<Instance> iterator = CLASSIFIER.getInstancePipe().newIteratorFrom(input);
		
		if(iterator.hasNext()) {
			Labeling labeling = CLASSIFIER.classify(iterator.next()).getLabeling();
			return DialogueActTag.valueOf(labeling.getBestLabel().toString());
		}
		
		return null;
	}
	
	// Construct a space-separated, String representation of the utterance
	private String utteranceToString(Utterance u) {
		String string = "";
		
		for(Token token : u.tokens) {
			string += token.token + " ";
		}
		
		// Add punctuation token to the end
		for(Punctuation suffix : Punctuation.values()) {
			if(u.utterance.endsWith(suffix.toString())) {
				string += suffix + " ";
				break;
			}
		}
		
		// Chop off the trailing space
		string = string.substring(0, string.length()-1) + "\n";

		return string;
	}
	
}
