package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * This class exports the switchboard data into a format which can be imported
 * into the MALLET format.
 * 
 * @author Creavesjohnson
 *
 */
class MalletExporter {

	private static final String outputFile = "/models/datag/switchboard-export.txt";
	private static final String SWITCHBOARD_DIR = "resources/swb1_dialogact_annot/scrubbed";

	public static void main(String[] args) {

		SwitchboardParser parser = null;

		// Parse the switchboard data
		try {
			parser = new SwitchboardParser(new File(SWITCHBOARD_DIR));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		PrintWriter output = null;

		// Open the output file
		try {
			output = new PrintWriter(new File(outputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		/*
		 * Print one line per dialogue act.
		 * [id][SPACE][label][SPACE][token][SPACE][token][SPACE]...
		 */
		int i = 0;
		for (DialogueActTag tag : DialogueActTag.values()) {
			List<DialogueAct> acts = parser.getActs(tag);

			for (DialogueAct act : acts) {

				List<String> words = act.getWords();
				String wordString = "";

				for (String word : words)
					wordString += word + " ";

				wordString = wordString.substring(0, wordString.length() - 1);

				output.printf("%s %s %s\n", i + "", act.getTag(), wordString);
				i++;
			}
		}

		output.close();

	}

}
