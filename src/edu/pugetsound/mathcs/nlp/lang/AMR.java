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

    // Mapping from semantic relation labels to the corresponding object
    // ex: ":arg0" -> SemanticRelation.arg0
    private static final HashMap<String, SemanticRelation> NAME_TO_RELATION =
        new HashMap<String, SemanticRelation>() {{
            for(SemanticRelation sr : SemanticRelation.values()) {
                put(sr.getLabel().substring(1), sr);
            }
        }};

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
     * The time since the MSR Splat API was last queried
     * For rate-limiting requests to every two seconds
     */
    private static long queryTime = 0;

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

        String stringForm = "(" + nodeValue[0] + " / " + nodeValue[1];

        // Use a DFT to print out the AMR graph
        for(SemanticRelation sr : SemanticRelation.values()) {
            if(semanticRelations.containsKey(sr) && semanticRelations.get(sr) != null) {
                stringForm += " :" + sr.toString() + " " + semanticRelations.get(sr).toString();
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
        semanticRelations.put(AMR.NAME_TO_RELATION.get(relationName), amr);
    }

    /**
     * Converts a string of English text to an array of AMRs, one for each sentence.
     * Invokes the python script calling Microsoft's msrsplat tool.
     * @param text the String to be converted into AMR
     * @return An array of AMRs, which are translations of each sentences' AMR
     */
    public static AMR[] convertTextToAMR(String text) {
        String scriptPath;
        scriptPath = PathFormat.absolutePathFromRoot("scripts/msrsplat.py");
        PythonInterpreter python = new PythonInterpreter();
        python.execfile(scriptPath);
        python.set("text", new PyString(text));
        if(Logger.debug()) {
            System.out.println("Querying MSR_SPLAT for the AMR string for '"+text+"'");
        }

        JSONParser parser = new JSONParser();
        try {
            if (System.currentTimeMillis() - queryTime < 5000)
                Thread.sleep(5000 - System.currentTimeMillis() + queryTime);
            queryTime = System.currentTimeMillis();
            python.exec("amr = main(text)");

            if(Logger.debug()) {
                System.out.println("Got the AMR String; parsing in a moment...");
            }
            Object obj = parser.parse(python.get("amr").toString());
            JSONArray amrStrs = (JSONArray) ((JSONObject)(((JSONArray) obj).get(0))).get("Value");
            if(Logger.debug()) {
                System.out.println("About to convert the parsed AMR Strings into AMR Objects");
            }

            String temp;
            AMR[] amrs = new AMR[amrStrs.size()];
            for (int i=0; i<amrs.length; i++) {
                if(Logger.debug()) {
                    System.out.println(amrStrs.get(i));
                    System.out.println(AMR.parseAMRString(amrStrs.get(i).toString()));
                }
                amrs[i] = AMR.parseAMRString(amrStrs.get(i).toString());
            }
            return amrs;
        } catch(ParseException pe) {
            if(Logger.debug()) {
                System.out.println("position: " + pe.getPosition());
                System.out.println(pe);
            }
        } catch(Exception e) {
            if(Logger.debug()) {
                System.out.println("ERROR: Microsoft msrsplat tool could not be reached for AMR conversion.");
            }
            return null;
        }
        return null;
    }


    /**
     * Returns an English-legible representation of this AMR using the text template
     * @return A String representation of this AMR
     */
    public String convertAMRToText(String[] text) {
        this.convertAMRToTextHelper(text);
        return Token.detokenize(text);
    }

    // Why do we have a public helper method?
    public void convertAMRToTextHelper(String[] text) {
        if (nodeAlignment != null && nodeAlignment.length > 0)
            text[nodeAlignment[0]] = nodeValue[1];
        for (AMR child: semanticRelations.values())
            child.convertAMRToTextHelper(text);
    }


    /**
     * Parses an AMR string to an AMR object
     * This is based on the Smatch parsing algorithm, which uses a shift-reduce style for parsing.
     * This method can easily break if the AMR is not well-formed. PLEASE delimit with spaces!
     * Note that due to the dependency on spaces, literal strings in the AMR may have extra
     * spaces added inside them.
     * @param text The AMR string to parse. There should be exactly one AMR in the string
     * @return The parsed AMR object
     */
    public static AMR parseAMRString(String text) {
        ArrayList<AMR> encounteredAMRs = new ArrayList<AMR>();
        return parseAMRStringRecurse(text, encounteredAMRs);
    }

    // Helper method to parseAMRString, simply to ensure that every recursion gets the correct
    // AMR ArrayList
    // encounteredAMRs is for cases where an AMR references an already-created AMR via its variable
    // ex: (w / want-01:ARG0 (b / boy):ARG1 (b2 / believe-01:ARG0 (g / girl):ARG1 b))
    private static AMR parseAMRStringRecurse(String text, ArrayList<AMR> encounteredAMRs) {
        // I am so sorry if you have to debug this

        // Convert text to lowercase for simplicity
        text = text.toLowerCase();

        // Force some extra spaces in there to help parsing
        text = text.replace(")", " )");
        text = text.replace("(", "( ");
        text = text.replace(":", " :");

        // Current state; denotes the last significant symbol encountered.
        // ":" indicates we should start processing the relation name
        // "/" indicates we should start processing the concept value
        // "(" indicates we are starting the AMR are should be processing the node's name and value
        // If any of these symbols are inside quotation marks, they are ignored
        char state = '(';

        // Current unreduced character sequence
        String cur_charseq = "";

        // The current AMR object we are filling
        AMR cur_amr = new AMR();
        // Bad for debugging, isn't indicative of whether it was parsed correctly
        //cur_amr.amrString = text;
        encounteredAMRs.add(cur_amr);

        // Flag for if we're insidez quotation marks
        boolean in_quote = false;

        // Used for jumping past the next AMR
        // When we encounter the first ( for an AMR we want to skip, this is set to 1
        // For each ( afterwards, we increment by 1. For each ), we decrement
        // When the counter reaches 0, we've passed the AMR
        // This is used for recursion
        int skip_amr = 0;

        // The last encountered string of characters before we hit a space
        String last_word = "";

        for(int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Continually ignore chars if we're skipping the current AMR
            // Used for when we recurse and don't want to parse the AMR we recursed
            if(skip_amr > 0) {
                if(c == '(') {
                    skip_amr++;
                } else if(c == ')') {
                    skip_amr--;
                }

                // skip to next character
                continue;
            }

            if(c == ')') {
                break;

            } else if(c == '"') {

                // Make an AMR for the quote if we're closing the quote
                if(in_quote) {
                    AMR quote = new AMR(String.valueOf(cur_charseq.charAt(0)), cur_charseq,
                                        AMRType.string);
                    if(state == ':') {
                        SemanticRelation sr = AMR.NAME_TO_RELATION.get(last_word);
                        if(sr == null) {
                            if(Logger.debug()) {
                                System.out.println("Error: Unrecognized relation " + last_word +
                                                   " while parsing AMR!");
                            }
                            return null;
                        } else {
                            // Add recursed to cur_amr's map of semantic relations
                            cur_amr.semanticRelations.put(sr, quote);
                        }

                        cur_charseq = "";
                        last_word = "";

                    } else {
                        if(Logger.debug()) {
                            System.out.println("Error: String without a relation!");
                        }
                        return null;
                    }
                }

                // Flip whether or not we're inside a quotation
                in_quote = !in_quote;

            } else if(c == '(') {

                // Not a significant character if inside quotation
                if(in_quote) {
                    cur_charseq += c;
                    continue;
                }

                // Get the attribute name and recurse to create the new AMR
                if(state == ':') {
                    AMR recursed = parseAMRStringRecurse(text.substring(i), encounteredAMRs);
                    // Skip over the part of text corresponding to recursed
                    skip_amr = 1;
                    //i++; // Prevent skip_amr from counting the first ( twice
                    SemanticRelation sr = AMR.NAME_TO_RELATION.get(last_word);
                    if(sr == null) {
                        System.out.println("Error: Unrecognized relation " + last_word  +
                                           " while parsing AMR!");
                        return null;
                    } else {
                        // If recursed == null, just don't add the AMR
                        if(recursed != null) {
                            // Add recursed to cur_amr's map of semantic relations
                            cur_amr.semanticRelations.put(sr, recursed);
                            // Add recursed to the list of encountered AMRs so we can reference it
                            // later
                            encounteredAMRs.add(recursed);
                        }
                    }

                    cur_charseq = "";
                    last_word = "";
                    state = '\0';
                }

            } else if(c == '/') {

                // Not a significant character if inside quotation
                if(in_quote) {
                    cur_charseq += c;
                    continue;
                }

                if(state == '(') {
                    // Add the node's variable name
                    cur_amr.nodeValue[0] = last_word;
                }

                state = '/';

            } else if(c == ':') {

                // Not a significant character if inside a quotation
                if(in_quote) {
                    cur_charseq += c;
                    continue;
                }

                state = ':';

            } else if(c == ' ') {

                // Not a significant character if inside a quotation
                if(in_quote) {
                    cur_charseq += c;
                    continue;
                }

                // If we have a space after some actual text,
                // it must be a variable, name, relation, or string
                if(!cur_charseq.equals("")) {

                    // If we're adding a relation which doesn't have a native AMR node attached
                    // ex: (f / frog :quant 30)
                    // The 30 doesn't have a node surrounding it, normally
                    // The second half of the && is a really quick 'n dirty way of checking that
                    // we're parsing the argument to a relation and not the relation name itself
                    if(state == ':' && text.charAt(i - cur_charseq.length() - 1) != ':') {
                        SemanticRelation sr = AMR.NAME_TO_RELATION.get(last_word);
                        if(sr == null) {
                            if(Logger.debug()) {
                                System.out.println("Error: Unrecognized relation " + last_word +
                                                   " while parsing AMR!");
                            }
                            return null;
                        } else {

                            boolean amrExisted = false;

                            // If the term is a variable pointing to an existing AMR, simply point
                            // to that AMR
                            for(AMR encountered : encounteredAMRs) {
                                if(encountered.nodeValue[0].equals(cur_charseq)) {
                                    cur_amr.semanticRelations.put(sr, encountered);
                                    amrExisted = true;
                                }
                            }

                            if(!amrExisted) {
                                // Create a new AMR to hold the term
                                AMR relation_val = new AMR("" + cur_charseq.charAt(0),
                                                           cur_charseq, AMRType.string);
                                cur_amr.semanticRelations.put(sr, relation_val);
                                encounteredAMRs.add(relation_val);
                            }

                            last_word = cur_charseq;
                            cur_charseq = "";
                        }

                    } else {

                        // In all other cases, we can set last_word
                        last_word = cur_charseq;
                        cur_charseq = "";

                        // If we're at the node value after a '/'
                        if(state == '/') {
                            cur_amr.nodeValue[1] = last_word;
                        }
                    }

                }

            } else {
                cur_charseq += c;
            }
        }

        // Finally done!
        return cur_amr;
    }

    /**
     * Takes a list of strings, converts them to AMR using msrsplat, then prints the result
     * @param args The list of strings to parse
     */
    public static void main(String args[]) {
        for (String s: args) {
            System.out.println(AMR.convertTextToAMR(s));
            //AMR result = parseAMRString(s);
            //System.out.println(result.toString());

        }
    }

}
