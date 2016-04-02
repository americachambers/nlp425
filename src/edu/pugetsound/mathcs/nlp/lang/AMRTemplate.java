package edu.pugetsound.mathcs.nlp.lang;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import java.util.HashMap;

/**
 * Represents an AMR node with missing node value and missing pointers through semantic relations
 * @author Thomas Gagne
 */
public class AMRTemplate {

    /**
     * The type of node this AMR is, for example: verb, noun, possibleness, amr-unknown
     */
    public AMR.AMRType nodeType;

    /**
     * The value of each semantic relation, which maps to an AMR type
     */
    public HashMap<AMR.SemanticRelation, AMR.AMRType> semanticRelations;

}
