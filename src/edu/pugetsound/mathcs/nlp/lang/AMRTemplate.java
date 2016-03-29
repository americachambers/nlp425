package edu.pugetsound.mathcs.nlp.lang;

/**
 * Represents an AMR node with missing node value and missing pointers through semantic relations
 * @author tgagne
 */
public class AMRTemplate {

    /**
     * The type of node this AMR is, for example: verb, noun, possibleness, amr-unknown
     */
    public AMRType nodeType;

    /**
     * The value of each semantic relation, which maps to an AMR type
     */
    public HashMap<AMR.SemanticRelation, AMRType> semanticRelations;

}
