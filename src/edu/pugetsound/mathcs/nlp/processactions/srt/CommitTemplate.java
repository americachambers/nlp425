package edu.pugetsound.mathcs.nlp.processactions.srt;

import edu.pugetsound.mathcs.nlp.processactions.srt.SemanticResponseTemplate;

/**
 * @author Thomas Gagne & Jon Sims
 * @version 04/26/16
 * A template for constructing a response which commits to an action, typically in response to a
 * user directive.
 * In the corpus, this almost always showed up as "I'll try to remember that" or
 * "I'll look into that".
 * This class should do some checking to see what kind of response should be returned given the
 * directive.
 */
public class CommitTemplate extends SemanticResponseTemplate {}
