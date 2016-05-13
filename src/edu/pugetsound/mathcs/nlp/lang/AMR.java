package edu.pugetsound.mathcs.nlp.lang;

import java.util.HashMap;
import java.util.List;
import java.lang.Thread;
import java.io.IOException;
import java.util.ArrayList;

//Requires Jython 2.5: http://www.jython.org/
//http://search.maven.org/remotecontent?filepath=org/python/jython-standalone/2.7.0/jython-standalone-2.7.0.jar
import org.python.util.PythonInterpreter;
import org.python.core.*;

//Requires Simple Json: https://code.google.com/archive/p/json-simple/
//https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/json-simple/json-simple-1.1.1.jar
import org.json.simple.*;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import edu.pugetsound.mathcs.nlp.lang.Token;
import edu.pugetsound.mathcs.nlp.lang.SemanticRelation;
import edu.pugetsound.mathcs.nlp.util.PathFormat;
import edu.pugetsound.mathcs.nlp.util.Logger;

/**
 * Represents a filled-in AMR
 * AMRs are represented by graphs, so an object of this type corresponds to a single node in
 * that graph. Edges in the graph are stored in the semanticRelations hashmap
 * @author Thomas Gagne & Jon Sims
 * @version 04/27/16
 */
public class AMR {

    /**
     * A list of the possible types an AMR node can be
     * Ex: verb, noun, adjectives (for copulas), possibleness, amr-unknown, etc.
     */
    public enum AMRType {
    	verb, noun, adjective,
    	possibleness, obligatoriness,
    	amr_unknown, // Used in wh-questions
    	string // Used in cases such as names
    }

    /**
     * The root value of this AMR node
     * Format should be like: ["g", "go-01"], which corresponds to "(g / go-01)"
     */
    public String[] nodeValue = new String[2];

    /**
     * The indices of this AMR's corrosponding words in an English sentence
     * The first index is the one corrosponding to the exact word, and the rest
     * correspond to other words most closely related to this AMR.
     * Returned through Microsoft's SPLAT tool via the :align tag, where the "*"
     * denotes the index of the exact word. From this tool, indecies start at 1.
     * See http://www.isi.edu/natural-language/mt/amr_eng_align.pdf for a better
     * description of alignment.
     */
    public Integer[] nodeAlignment;

    /**
     * The type of node the AMR nodeValue as, for example: verb, noun, possibleness, amr-unknown
     */
    public AMRType nodeType;

    /**
     * The value of each semantic relation, which maps to another AMR or NULL
     */
    public HashMap<SemanticRelation, AMR> semanticRelations = new HashMap<SemanticRelation, AMR>();

    /**
     * The String representation of the AMR as given my MSR Splat
     */
    public String amrString;

    /**
     * Constructor for an AMR node where several values will begin initialized
     * @param nodeVar The variable name of the node. Ex: the 'b' in (b / boy)
     * @param nodeValue The value of this node. Ex: the 'boy' in (b / boy)
     * @param nodeType Whether nodeValue is a verb, noun, string, etc.
     */
    public AMR(String nodeVar, String nodeValue, AMR.AMRType nodeType) {
        this.nodeValue[0] = nodeVar;
        this.nodeValue[1] = nodeValue;
        this.nodeType = nodeType;
    }

    /**
     * Empty constructor for an AMR node where nodeValue and nodeType are currently unknown.
     */
    public AMR(){};

    /**
     * Returns a human-readable representation of this AMR node in nested notation
     * @return A String representation of this AMR
     */
    public String toString() {
        if (amrString != null) {
            return amrString;
        }

        // Sometimes, AMRs can point to themselves
        // Ex: Msrsplat's parsing of "Ok, then did you have anything you wanted to say?"
        ArrayList<AMR> alreadyAdded = new ArrayList<AMR>();
        return toStringRecurse(alreadyAdded);
    }

    // Helper method to toString()
    private String toStringRecurse(ArrayList<AMR> alreadyAdded) {
        String stringForm = "(" + nodeValue[0] + " / " + nodeValue[1];

        // Use a DFT to print out the AMR graph
        for(SemanticRelation sr : SemanticRelation.values()) {
            if(semanticRelations.containsKey(sr) && semanticRelations.get(sr) != null) {
                stringForm += " :" + sr.toString() + " ";
                AMR nextAMR = semanticRelations.get(sr);

                if(alreadyAdded.contains(nextAMR)) {
                    stringForm += nextAMR.nodeValue[0];
                } else {
                    alreadyAdded.add(nextAMR);
                    stringForm += nextAMR.toStringRecurse(alreadyAdded);
                }
            }
        }

        stringForm += ")";
        return stringForm;
    }

    /**
     * Adds a semantic relation from this AMR to `amr`
     * @param relationName The string name of the relation
     * @param amr The endpoint AMR of this relation
     */
    public void addSemanticRelation(String relationName, AMR amr) {
        semanticRelations.put(SemanticRelation.getByLabel(relationName), amr);
    }

}
