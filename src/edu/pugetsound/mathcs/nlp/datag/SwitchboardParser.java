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

class SwitchboardParser {

	private static final String SB_SUFFIX = ".utt";
	private static final String START_SENTINEL = "=";
	private static final String ACT_SPLIT = "[ ]{10}";
	private static final String TOKEN_REGEX = "\\w+";
	private static final String[] REMOVALS = {"\\{", "[A-Z]\\s", "\\}", "!", ",", "\\[", "\\]"};
	
	private Map<DialogueActTag,List<DialogueAct>> tagToActs;
	private Map<String,Integer> tokenToIndex;
	private Set<String> tokenSet;
	
	// DEBUG
	private int totalTags = 0, errorTags = 0;
	
	/**
	 * Constructs a parser which traverses the given directory recursively for .utt files.
	 * @param dataDirectory The directory in which to traverse for .utt files
	 * @throws FileNotFoundException if the directory does not exist
	 */
	public SwitchboardParser(File dataDirectory) throws FileNotFoundException {
		tagToActs = new HashMap<DialogueActTag,List<DialogueAct>>();
		tokenToIndex = new HashMap<String,Integer>();
		tokenSet = new LinkedHashSet<String>();
		
		System.out.println("[DATAG] Loading Switchboard data...");
		
		if(dataDirectory.isDirectory()) {
			parseDir(dataDirectory);
		} else {
			parseFile(dataDirectory);
		}
		
		int index = 0;
		for(String token : tokenSet) {
			tokenToIndex.put(token, index);
			index++;
		}
		
		double parseError = (1.0 - (double)(errorTags) / (double)(totalTags)) * 100;
		
		System.out.printf("[DATAG] %.2f%% of tags parsed.\n", parseError);
	}
	
	/**
	 * Gets all DialogueActs which have been parsed by this parser.
	 * @return A List of all DialogueActs
	 */
	public List<DialogueAct> getActs() {
		List<DialogueAct> acts = new LinkedList<DialogueAct>();
		
		for(List<DialogueAct> actList : tagToActs.values())
			acts.addAll(actList);
		
		return acts;
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
	
	public List<DialogueAct> getActsExcluding(DialogueActTag... tags) {
		Set<DialogueActTag> tagSet = new HashSet<DialogueActTag>(Arrays.asList(tags));
		List<DialogueAct> actList = new LinkedList<DialogueAct>();
		
		for(DialogueAct act : this.getActs())
			if(!tagSet.contains(act.getTag()))
				actList.add(act);
		
		return actList;
	}
	
	/**
	 * Gets the TokenIndexMap created while parsing the data
	 * @return A TokenIndexMap
	 */
	public TokenIndexMap getTokenIndexMap() {
		return new TokenIndexMap(this.tokenToIndex);
	}
	
	// Parses a single .utt file and stuffs the recognizable dialogue acts into tagToActs
	private void parseFile(File file) throws FileNotFoundException {
		
		Scanner input = new Scanner(file);
		String line;
		boolean dataReached = false;
		
		while(input.hasNextLine()) {
			line = input.nextLine();
			
			dataReached = true;
			
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
					
					if(tagString.length() > 0 && tagString.charAt(0) != '^' && tagString.indexOf('^') != -1)
						tagString = tagString.substring(tagString.indexOf('^'));
					
					if(tagString.endsWith(")"))
						tagString = tagString.substring(0, tagString.length() - 1);
					
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
		
		for(String token : split) {
			if(token.matches(TOKEN_REGEX)) {
				tokens.add(token.toLowerCase());
				this.tokenSet.add(token.toLowerCase());
			}
		}
		
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