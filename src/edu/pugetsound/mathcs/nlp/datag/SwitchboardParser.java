package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import edu.pugetsound.mathcs.nlp.util.Logger;
import edu.pugetsound.mathcs.nlp.util.PathFormat;

/**
 * This object, on construction, parses scrubbed switchboard data into
 * DialogueActs and makes them accessible by DialogueActTag
 * 
 * @author Creavesjohnson
 *
 */
class SwitchboardParser {

	private static final String SB_SUFFIX = ".utt";
	private static final String ACT_SPLIT = "\t";

	// Only grabs words and words and punctuation
	private static final String TOKEN_REGEX = "[\\w\\.\\?,!']+";
	private static final String PUNCTUATED_REGEX = ".*[\\?\\.,!]$";

	// Remove these from the utterance when present
	private static final String[] REMOVALS = { "\\{", "\\}", "," };
	private static final char CARAT = '^';
	private static final String CLOSE_PARENTHESIS = ")";
	private static final String AT = "@";
	private static final String SPACES = "[\\s\\.\\?]+";

	private final Map<DialogueActTag, List<DialogueAct>> tagToActs;
	private final Map<String, Integer> tokenToIndex;
	private final Map<String, Integer> tokenToCount;
	private final Set<String> tokenSet;

	/**
	 * Constructs a parser which traverses the given directory recursively for
	 * .utt files.
	 * 
	 * @param dataDirectory
	 *            The directory in which to traverse for .utt files
	 * @throws FileNotFoundException
	 *             if the directory does not exist
	 */
	public SwitchboardParser(File dataDirectory) throws FileNotFoundException {
		tagToActs = new HashMap<DialogueActTag, List<DialogueAct>>();
		tokenToIndex = new HashMap<String, Integer>();
		tokenToCount = new HashMap<String, Integer>();
		tokenSet = new LinkedHashSet<String>();

		if (Logger.debug()) {
			System.out.println("[DATAG] Loading Switchboard data...");
		}

		if (dataDirectory.isDirectory()) {
			parseDir(dataDirectory);
		} else {
			parseFile(dataDirectory);
		}

		int index = 0;
		for (String token : tokenSet) {
			tokenToIndex.put(token, index);
			index++;
		}
	}

	/**
	 * Gets all DialogueActs which have been parsed by this parser.
	 * 
	 * @return A List of all DialogueActs
	 */
	public List<DialogueAct> getActs() {
		List<DialogueAct> acts = new LinkedList<DialogueAct>();

		for (List<DialogueAct> actList : tagToActs.values())
			acts.addAll(actList);

		return acts;
	}

	/**
	 * Gets a list of acts which have been labeled with a given tag
	 * 
	 * @param tags
	 *            Desired tags
	 * @return A list of DialogueActs which have been labeled with the given
	 *         tags
	 */
	public List<DialogueAct> getActs(DialogueActTag... tags) {
		List<DialogueAct> acts = new LinkedList<DialogueAct>();

		for (DialogueActTag tag : tags)
			if (tagToActs.get(tag) != null)
				acts.addAll(tagToActs.get(tag));

		return acts;
	}

	/**
	 * Get a list of all DialogueActs which do have have the supplied
	 * DialogueActTags
	 * 
	 * @param tags
	 *            Undesired tags
	 * @return A list of DialogueActs which have not been labeled with the given
	 *         tags
	 */
	public List<DialogueAct> getActsExcluding(DialogueActTag... tags) {
		Set<DialogueActTag> tagSet = new HashSet<DialogueActTag>(Arrays.asList(tags));
		List<DialogueAct> actList = new LinkedList<DialogueAct>();

		for (DialogueAct act : this.getActs())
			if (!tagSet.contains(act.getTag()))
				actList.add(act);

		return actList;
	}

	/**
	 * Gets the TokenIndexMap created while parsing the data
	 * 
	 * @return A TokenIndexMap
	 */
	public TokenIndexMap getTokenIndexMap() {
		return new TokenIndexMap(this.tokenToIndex, this.tokenToCount);
	}

	// Parses a single .utt file and stuffs the recognizable dialogue acts into
	// tagToActs
	private void parseFile(File file) throws FileNotFoundException {

		Scanner input = new Scanner(file);
		String line;

		Map<Character, DialogueAct> lastSpoken = new HashMap<Character, DialogueAct>();
		Character prevSpeaker = null;
		
		DialogueActTag prevTag = DialogueActTag.NULL;

		while (input.hasNextLine()) {
			line = input.nextLine();

			String[] split = line.split(ACT_SPLIT);
			// Make sure this is a real line
			if (split != null && split.length > 0) {
				String tagString = split[0];

				// Chop off any ^
				if (tagString.length() > 0 && tagString.charAt(0) != CARAT
						&& tagString.indexOf(CARAT) != -1)
					tagString = tagString.substring(tagString.indexOf(CARAT));

				// Remove parentheses
				if (tagString.endsWith(CLOSE_PARENTHESIS))
					tagString = tagString.substring(0, tagString.length() - 1);

				// If a speaker, label, and utterance exists
				if (split != null && split.length > 2) {

					char speaker;
					if (split[1].startsWith(AT))
						speaker = split[1].charAt(1);
					else
						speaker = split[1].charAt(0);

					String utterance = split[2];

					if (utterance.matches(PUNCTUATED_REGEX)) {
						char punctuation = utterance.charAt(utterance.length() - 1);
						utterance = utterance.substring(0, utterance.length() - 1);
						utterance += " " + punctuation;
					}

					for (String removal : REMOVALS)
						utterance = utterance.replaceAll(removal, "");

					List<String> utteranceTokens = tokenizeUtterance(utterance);

					try {
						if (utteranceTokens.size() > 0) {

							// Combine CONTINUED_FROM_PREVIOUS tags based on
							// speaker
							DialogueActTag tag = DialogueActTag.fromLabel(tagString);
							if (tag.equals(DialogueActTag.CONTINUED_FROM_PREVIOUS)) {
								if (lastSpoken.get(speaker) != null) {
									lastSpoken.get(speaker).appendWords(utteranceTokens);
								}
							} else {
								if (lastSpoken.get(speaker) != null) {
									putAct(lastSpoken.get(speaker));
								}

								DialogueAct newAct = new DialogueAct(tag, prevTag, utteranceTokens);
								prevTag = tag;
								lastSpoken.put(speaker, newAct);
								prevSpeaker = speaker;
							}

						}
					} catch (IllegalArgumentException e) {
//						if (Logger.debug()) {
//							System.err
//									.println("[DATAG] Could not parse switchboard line\n\tDue to: "
//											+ e.toString() + "\n\tFor line: \"" + line + "\"");
//						}
					}

				}
			}
		}

		putAct(lastSpoken.get(prevSpeaker));

		input.close();
	}
	
	// Recursively traverses a directory structure and parses .utt files
	private void parseDir(File dir) throws FileNotFoundException {
		File[] files = dir.listFiles();

		for (File file : files)
			if (file.isDirectory())
				parseDir(file);
			else if (file.getName().endsWith(SB_SUFFIX))
				parseFile(file);

	}

	// Split string into tokens based on whitespace and make sure everything's lowercase
	private List<String> tokenizeUtterance(String utterance) {
		List<String> tokens = new LinkedList<String>();

		String[] split = utterance.split(SPACES);

		for (String token : split) {
			token = token.toLowerCase();
			if (token.matches(TOKEN_REGEX)) {
				tokens.add(token.toLowerCase());
				this.tokenSet.add(token.toLowerCase());
				
				if(!tokenToCount.containsKey(token)) {
					tokenToCount.put(token, 1);
				} else {
					tokenToCount.replace(token, tokenToCount.get(token) + 1);
				}
			}
		}

		return tokens;
	}

	// Puts a DialogueAct into tagToActs
	private void putAct(DialogueAct act) {
		List<DialogueAct> actList = tagToActs.get(act.getTag());

		if (actList == null) {
			actList = new LinkedList<DialogueAct>();
			tagToActs.put(act.getTag(), actList);
		}

		actList.add(act);
	}

}