package edu.pugetsound.mathcs.nlp.datag;

import java.util.List;

import edu.pugetsound.mathcs.nlp.lang.Utterance;
import java.util.*;

class DumbClassifier extends Classifier {

//	public static void main(String)
	
	public DialogueActTag classify(Utterance u) {
		
		String utterance = u.utterance;
		
		if(utterance.contains("?")) {
			if(!startsWithAny(utterance, "is", "are") && utterance.contains(" or ")) {
				return DialogueActTag.QUESTION_ALTERNATIVE;
			} else if(startsWithAny(utterance, "is", "isn't", "are", "aren't", "do", "don't", "did", "didn't", "does", "doesn't", "can", "can't", "would", "wouldn't", "should", "shouldn't", "shall", "shan't")) {
				if(utterance.contains(" or "))
					return DialogueActTag.QUESTION_YES_NO_OR;
				else
					return DialogueActTag.QUESTION_YES_NO;
			} else if(startsWithAny(utterance, "who", "what", "where", "when", "why", "how")) {
				return DialogueActTag.QUESTION_WH;
			} else {
				return randomTag(DialogueActTag.QUESTION, DialogueActTag.QUESTION_ALTERNATIVE, DialogueActTag.QUESTION_OPEN_ENDED, DialogueActTag.QUESTION_RHETORICAL);
			}
		} else if(utterance.endsWith(".")) {
			return DialogueActTag.STATEMENT;
		} else {
			return randomTagExcluding(
					DialogueActTag.QUESTION,
					DialogueActTag.QUESTION_ALTERNATIVE,
					DialogueActTag.QUESTION_OPEN_ENDED,
					DialogueActTag.QUESTION_RHETORICAL,
					DialogueActTag.QUESTION_WH,
					DialogueActTag.QUESTION_YES_NO,
					DialogueActTag.QUESTION_YES_NO_OR,
					DialogueActTag.STATEMENT);
		}
	}
	
	private boolean startsWithAny(String string, String... prefixes) {
		for(String prefix : prefixes)
			if(string.toLowerCase().startsWith(prefix))
				return true;
		return false;
	}
	
	private DialogueActTag randomTag(DialogueActTag... tags) {
		int randIndex = (int)(Math.random() * tags.length);
		return tags[randIndex];
	}
	
	private DialogueActTag randomTagExcluding(DialogueActTag... tags) {
		
		List<DialogueActTag> tagList = new LinkedList<DialogueActTag>(Arrays.asList(DialogueActTag.values()));
		
		for(DialogueActTag tag : tags)
			tagList.remove(tag);
		
		System.out.println(tagList.size());
		
		return randomTag(tagList.toArray(new DialogueActTag[tagList.size()]));

	}
	
}