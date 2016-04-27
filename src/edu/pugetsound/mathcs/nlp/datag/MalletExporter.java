package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class MalletExporter {
	
	private static final String outputFile = "models/datag/mallet-import.txt";

	public static void main(String[] args) {
		
		SwitchboardParser parser = null;
		
		try {
			parser = new SwitchboardParser(new File("resources/swb1_dialogact_annot/scrubbed"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		PrintWriter output = null;
		
		try {
			output = new PrintWriter(new File(outputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		int i = 0;
		
		for(DialogueActTag tag : DialogueActTag.values()) {
			List<DialogueAct> acts = parser.getActs(tag);
			
			for(DialogueAct act : acts) {
				
				List<String> words = act.getWords();
				String wordString = "";
				
				for(String word : words)
					wordString += word + " ";
				
				wordString = wordString.substring(0, wordString.length() - 1);
				
				output.printf("%s %s %s\n", i + "", act.getTag(), wordString);
				i++;
			}
		}
		
		output.close();
		
	}

}
