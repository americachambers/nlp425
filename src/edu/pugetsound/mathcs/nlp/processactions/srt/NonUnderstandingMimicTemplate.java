package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for expressing nonunderstanding of what the user said.
 * This differs from NonUnderstandingTemplate in that this response is more explicit about what
 * part it did not understand by referencing something in the user's input.
 * For example, if the user says "I enjoy gooflagggin" this would return "Gooflagggin?"
 */
public class NonUnderstandingMimicTemplate extends SemanticResponseTemplate {}
