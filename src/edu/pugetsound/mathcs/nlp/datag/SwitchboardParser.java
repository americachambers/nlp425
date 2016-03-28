package edu.pugetsound.mathcs.nlp.datag;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class SwitchboardParser {

	private static final String SB_SUFFIX = ".utt";
	private static final String START_SENTINEL = "=";
	private static final String ACT_SPLIT = "[ ]{10}";
	private static final String TOKEN_REGEX = "\\w+";
	private static final String[] REMOVALS = {"\\{", "[A-Z]\\s", "\\}", "\\.", ",", "\\[", "\\]"};
	
	private Map<DialogueActTag,List<DialogueAct>> tagToActs;
	
	// DEBUG
	private int totalTags = 0, errorTags = 0;
	
	/**
	 * Constructs a parser which traverses the given directory recursively for .utt files.
	 * @param dataDirectory The directory in which to traverse for .utt files
	 * @throws FileNotFoundException if the directory does not exist
	 */
	public SwitchboardParser(File dataDirectory) throws FileNotFoundException {
		tagToActs = new HashMap<DialogueActTag,List<DialogueAct>>();
		
		System.out.println("[DATAG] Loading Switchboard data...");
		
		if(dataDirectory.isDirectory()) {
			parseDir(dataDirectory);
		} else {
			parseFile(dataDirectory);
		}
		
		double parseError = (1.0 - (double)(errorTags) / (double)(totalTags)) * 100;
		
		System.out.printf("[DATAG] %.2f%% of tags parsed.\n", parseError);
	}
	
	/**
	 * Gets a list of acts which have been labeled with a given tag
	 * @param tags
	 * @return A list of DialogueActs which have been labeled with a given tag
	 */
	public List<DialogueAct> getActs(DialogueActTag... tags) {
		List<DialogueAct> acts = new LinkedList<DialogueAct>();
		
		for(DialogueActTag tag : tags)
			if(tagToActs.get(tag) != null)
				acts.addAll(tagToActs.get(tag));
		
		return acts;
	}
	
	// Parses a single .utt file and stuffs the recognizable dialogue acts into tagToActs
	private void parseFile(File file) throws FileNotFoundException {
		
		Scanner input = new Scanner(file);
		String line;
		boolean dataReached = false;
		
		while(input.hasNextLine()) {
			line = input.nextLine();
			
			// Skip past all of the metadata
			if(!dataReached && line.startsWith(START_SENTINEL)) {
				dataReached = true;
				input.nextLine();
				input.nextLine();
				continue;
			}
			
			if(dataReached) {
				String[] split = line.split(ACT_SPLIT);
				if(split != null && split.length > 0) {
					String tagString = split[0];
					
					if(split != null && split.length > 1) {
						int colonIndex = split[1].indexOf(':');
						if(colonIndex != -1) {
							
							String utterance = split[1].substring(colonIndex + 1);
							for(String removal : REMOVALS)
								utterance = utterance.replaceAll(removal, "");
							
							List<String> utteranceTokens = tokenizeUtterance(utterance);
							
							// DEBUG
							this.totalTags++;
							
							try {
								DialogueActTag tag = DialogueActTag.fromLabel(tagString);
								putAct(new DialogueAct(tag, utteranceTokens));
							} catch(IllegalArgumentException e) {
								this.errorTags++;
							}
							
							
						}
					}
				}
			}
		}
		input.close();
	}
	
	// Recursively traverses a directory structure and parses .utt files
	private void parseDir(File dir) throws FileNotFoundException {
		File[] files = dir.listFiles();
		
		for(File file : files)
			if(file.isDirectory())
				parseDir(file);
			else if(file.getName().endsWith(SB_SUFFIX))
				parseFile(file);

	}
	
	// Preliminary definition of an utterance tokenizer
	//TODO: Modify this so the string array makes more sense / misses fewer words
	private List<String> tokenizeUtterance(String utterance) {
		List<String> tokens = new LinkedList<String>();
		
		String[] split = utterance.split("[ ]+");
		
		for(String token : split)
			if(token.matches(TOKEN_REGEX))
				tokens.add(token.toLowerCase());
		
		return tokens;
	}
	
	// Puts a DialogueAct into tagToActs
	private void putAct(DialogueAct act) {
		List<DialogueAct> actList = tagToActs.get(act.getTag());
		
		if(actList == null) {
			actList = new LinkedList<DialogueAct>();
			tagToActs.put(act.getTag(), actList);
		}
		
		actList.add(act);
	}
	
}