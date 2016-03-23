package edu.pugetsound.mathcs.nlp.datag;

public enum DialogueActTag {
	QUESTION("q"),
	STATEMENT("s"),
	BACKCHANNEL("b"),
	FORWARD_LOOKING("f"),
	AGREEMENTS("a"),
	INDETERMINATE("%"),
	COMMENT("*"),
	CONTINUED_FROM_PREVIOUS("+"),
	COLLABORATIVE_COMPLETION("^2"),
	ABOUT_COMMUNICATION("^c"),
	DECLARATIVE_QUESTION("^d"),
	ELABORATED_REPLY_Y_N_QUESTION("^e"),
	TAG_QUESTION("^g"),
	HOLD("^h"),
	MIMIC_OTHER("^m"),
	QUOTATION("^q"),
	REPEAT_SELF("^r"),
	ABOUT_TASK("^t"),
	ACCEPT_PART("aap"),
	ACTION_DIRECTIVE("ad"),
	ACCEPT("aa"),
	MAYBE("am"),
	REJECT("ar"),
	REJECT_PART("arp"),
	CONTINUER("b"),
	REPEAT_PHRASE("b^m"),
	ASSESSMENT_APPRECIATION("ba"),
	CORRECT_MISSPEAKING("bc"),
	DOWNPLAYING_SYMPATHY("bd"),
	REFORMULATE_SUMMARIZE("bf"),
	RHETORICAL_QUESTION_CONTINUER("bh"),
	ACKNOWLEDGE_ANSWER("bk"),
	SIGNAL_NON_UNDERSTANDING("br"),
	SIGNAL_NON_UNDERSTANDING_MIMIC("br^m"),
	NON_UNDERSTANDING_MEDIUM("br^c"),
	SYMPATHETIC_COMMENT("by"),
	COMMIT("cc"),
	OFFER("co"),
	APOLOGY("fa"),
	CONVENTIONAL_CLOSING("fc"),
	EXCLAMATION("fe"),
	OTHER_FORWARD_FUNCTION("fo"),
	CONVENTIONAL_OPENING("fp"),
	THANKS("ft"),
	WELCOME("fw"),
	EXPLICIT_PERFORMATIVE("fx"),
	DESCRIPTIVE_AFFIRMATIVE_ANSWER("na"),
	ANSWER_DISPREFERRED("nd"),
	DESCRIPTIVE_NEGATIVE_ANSWER("ng"),
	NO("nn"),
	INDETERMINATE_RESPONSE("no"),
	YES("ny"),
	OTHER("o"),
	OPEN_OPTION("oo"),
	QUESTION_RHETORICAL("qh"),
	QUESTION_OPEN_ENDED("qo"),
	QUESTION_ALTERNATIVE("qr"),
	QUESTION_YES_NO_OR("qrr"),
	QUESTION_WH("qw"),
	QUESTION_YES_NO("qy"),
	NARRATIVE_DESCRIPTIVE("sd"),
	VIEWPOINT("sv"),
	TALK_SELF("t1"),
	TALK_THIRD_PARTY("t3"),
	NON_SPEECH("x");
	
	private String label;
	
	DialogueActTag(String label) {
		this.label = label;
	}
	
	public static DialogueActTag fromLabel(String label) throws IllegalArgumentException {
	    if (label != null)
	      for (DialogueActTag tag : DialogueActTag.values())
	        if (tag.label.equalsIgnoreCase(label))
	          return tag;
	    throw new IllegalArgumentException("\"" + label + "\" is not a valid dialogue act tag label.");
	  }
}