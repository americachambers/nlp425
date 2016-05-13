package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.pugetsound.mathcs.nlp.util.PathFormat;

/**
 * This class exports the switchboard data into a format which can be imported
 * into the MALLET format as well as token frequencies to a CSV file in the
 * data/datag directory.
 * 
 * @author Creavesjohnson
 * @version 05/13/2016
 */
class MalletExporter {

	// File paths for input and output files
	private static final String OUTPUT_FILE = PathFormat
			.absolutePathFromRoot("models/datag/switchboard-mallet-import.txt");
	private static final String SWITCHBOARD_DIR = PathFormat
			.absolutePathFromRoot("resources/swb1_dialogact_annot/scrubbed");
	private static final String TOKEN_COUNT_CSV = PathFormat
			.absolutePathFromRoot("data/datag/token-counts.csv");

	// Remove tokens with absolute counts below this threshold
	private static final int TAIL_THRESHOLD = 15;

	// Remove tokens which appear in more than this proportion of acts
	private static final double HEAD_THRESHOLD = 0.08;

	/**
	 * A key-value two-tuple used primarily to sort keys by their associated
	 * integer values
	 * 
	 * @author Creavesjohnson
	 * @version 05/13/2016
	 * @param <K>
	 *            The class of the key in the tuple
	 */
	private static class CountTuple<K> implements Comparable<CountTuple<K>> {
		private K key;
		private int count;

		/**
		 * Constructs a tuple
		 * 
		 * @param key
		 *            The tuple key
		 * @param count
		 *            The count for the key
		 */
		public CountTuple(K key, int count) {
			this.key = key;
			this.count = count;
		}

		@Override
		public int compareTo(CountTuple<K> other) {
			return this.count - other.count;
		}
	}

	/**
	 * Fills the token-count map and (token, count) list data structures
	 * 
	 * @param parser
	 *            A SwitchboardParser
	 * @param tokenActCounts
	 *            List to populate with sorted token-count tuples
	 * @param tokenToActCount
	 *            Map from token strings to the number of acts they appear in,
	 *            to be populated
	 */
	private static void fillTokenActCounts(SwitchboardParser parser,
			List<CountTuple<String>> tokenActCounts, Map<String, Integer> tokenToActCount) {
		// For EVERY act
		for (DialogueAct act : parser.getActs()) {
			Set<String> actTokens = new HashSet<String>();

			// Collect tokens uniquely
			for (String token : act.getWords()) {
				actTokens.add(token);
			}

			// For each unique token, increment its count
			for (String token : actTokens) {
				if (!tokenToActCount.containsKey(token)) {
					tokenToActCount.put(token, 1);
				} else {
					tokenToActCount.put(token, tokenToActCount.get(token) + 1);
				}
			}
		}

		// Convert act counts to something sortable, and sort them
		for (String token : tokenToActCount.keySet()) {
			tokenActCounts.add(new CountTuple<String>(token, tokenToActCount.get(token)));
		}

		Collections.sort(tokenActCounts);
	}

	/**
	 * Exports a data file to be imported into the MALLET format
	 * 
	 * @param parser
	 *            A SwitchboardParser holding all of the relevant DialogueActs
	 * @param removals
	 *            The set of words to be excluded from the final data
	 */
	private static void exportMallet(SwitchboardParser parser, Set<String> removals) {
		PrintWriter output = null;

		// Open the output file
		try {
			output = new PrintWriter(new File(OUTPUT_FILE));
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
				String wordString = act.getPreviousTag().name() + " ";
				int zeroLength = wordString.length();

				for (String word : words) {
					if (!removals.contains(word)) {
						wordString += word + " ";
					}
				}

				if (wordString.length() > zeroLength) {
					wordString = wordString.substring(0, wordString.length() - 1);

					output.printf("%s %s %s\n", i + "", act.getTag().name(), wordString);
					i++;

				}
			}
		}

		output.close();
	}

	/**
	 * Exports a CSV containing word counts
	 * 
	 * @param absoluteWordCounts
	 *            A list of absolute word count tuples
	 * @param tokenToActCount
	 *            A map from token strings to the number of acts they appear in
	 * @param totalActs
	 *            The total number of DialogueActs
	 */
	private static void exportCSV(List<CountTuple<String>> absoluteWordCounts,
			Map<String, Integer> tokenToActCount, int totalActs) {

		PrintWriter output;
		output = null;

		try {
			output = new PrintWriter(new File(TOKEN_COUNT_CSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Column headers
		output.println("\"Token\",\"Count\",\"Number of Dialogue Acts\",\"Total Acts\"");

		// One line per token
		for (CountTuple<String> count : absoluteWordCounts) {
			if (tokenToActCount.containsKey(count.key)) {
				output.printf("\"%s\",%d,%d,%d\n", count.key, count.count,
						tokenToActCount.get(count.key), totalActs);
			}
		}

		output.close();
	}

	/**
	 * Calculates absolute counts for all words in a SwitchboardParser object
	 * 
	 * @param parser
	 *            A SwitchboardParser object
	 * @return A sorted list of (token, count) tuples
	 */
	private static List<CountTuple<String>> countWordsAbsolute(SwitchboardParser parser) {
		List<CountTuple<String>> tokenCounts = new ArrayList<CountTuple<String>>();

		TokenIndexMap indexMap = parser.getTokenIndexMap();

		for (String token : indexMap.getTokens()) {
			tokenCounts.add(new CountTuple<String>(token, indexMap.countForToken(token)));
		}

		Collections.sort(tokenCounts);

		return tokenCounts;
	}

	/**
	 * Execution entry point. Parses the Switchboard data, removing any tokens
	 * which appear too frequently or infrequently, then exports the parsed
	 * DialogueActs in a format which can be imported into the MALLET format.
	 * Additionally it writes tokens and their corresponding counts to a CSV
	 * file for analysis.
	 * 
	 * @param args
	 *            Unused
	 */
	public static void main(String[] args) {

		SwitchboardParser parser = null;

		// Parse the switchboard data
		try {
			parser = new SwitchboardParser(new File(SWITCHBOARD_DIR));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Set of strings to remove from the training data
		Set<String> removals = new HashSet<String>();

		// Count absolute words and remove those below count threshold
		List<CountTuple<String>> absoluteWordCounts = countWordsAbsolute(parser);
		int totalWords = 0;
		for (CountTuple<String> count : absoluteWordCounts) {
			if (count.count < TAIL_THRESHOLD) {
				removals.add(count.key);
				totalWords += count.count;
			} else {
				break;
			}
		}

		// Map from token strings to the number of acts they appear in
		Map<String, Integer> tokenToActCount = new HashMap<String, Integer>();

		// Sorted list of token-count tuples
		List<CountTuple<String>> tokenActCounts = new ArrayList<CountTuple<String>>();

		fillTokenActCounts(parser, tokenActCounts, tokenToActCount);

		int totalActs = parser.getActs().size();

		// Remove tokens which appear in too many dialogue acts
		for (CountTuple<String> count : tokenActCounts) {
			double proportion = (double) (count.count) / (double) (totalActs);

			if (proportion >= HEAD_THRESHOLD) {
				removals.add(count.key);
			}
		}

		System.out.println("Removing " + removals.size() + " of " + totalWords + " total words.");

		// Write the mallet import file
		exportMallet(parser, removals);

		// Export a CSV for looking at token statistics
		exportCSV(absoluteWordCounts, tokenToActCount, totalActs);

	}

}
