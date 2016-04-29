package edu.pugetsound.mathcs.nlp.processactions;

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
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.lang.SemanticRelation;
import edu.pugetsound.mathcs.nlp.util.PathFormat;
import edu.pugetsound.mathcs.nlp.util.Logger;

/**
 * A utility class for converting AMRs to text, converting text to AMRs, and for parsing
 * AMR strings to AMR objects
 * @author Thomas Gagne & Jon Sims
 * @version 04/29/16
 */
public class AMRParser {

    /**
     * The time since the MSR Splat API was last queried
     * For rate-limiting requests to every two seconds
     */
    private static long queryTime = 0;

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
                temp = amrStrs.get(i).toString();
                if (temp == null || temp.length() < 2 )
                    //throw new ParseException("MSR_SPLAT AMR String doesn't parse properly", 0);
                    throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
                if(Logger.debug()) {
                    System.out.println(amrStrs.get(i));
                    System.out.println(AMRParser.parseAMRString(amrStrs.get(i).toString()));
                }
                amrs[i] = AMRParser.parseAMRString(temp);
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
    public static String convertAMRToText(AMR amr, String[] text) {
        AMRParser.convertAMRToTextHelper(amr, text);
        return Token.detokenize(text);
    }

    // Why do we have a public helper method?
    // Also, I think this should have a stopping condition for recursion, since some
    // AMRs can have edges pointing to themselves
    public static void convertAMRToTextHelper(AMR amr, String[] text) {
        if (amr.nodeAlignment != null && amr.nodeAlignment.length > 0)
            text[amr.nodeAlignment[0]] = amr.nodeValue[1];
        for (AMR child: amr.semanticRelations.values())
            AMRParser.convertAMRToTextHelper(child, text);
    }

    /**
     * Parses an AMR string to an AMR object
     * This is based on the Smatch parsing algorithm, which uses somthing similar to a
     * shift-reduce style for parsing.
     * This method can easily break if the AMR is not well-formed. PLEASE delimit with spaces!
     * Note that due to the dependency on spaces, literal strings in the AMR may have extra
     * spaces added inside them after parsing.
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
        // Force some extra spaces in there for parsing and convert to lower case for simplicity
        text = text.replace(")", " )")
            .replace("(", "( ")
            .replace(":", " :")
            .toLowerCase();

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
                                        AMR.AMRType.string);
                    if(state == ':') {
                        SemanticRelation sr = SemanticRelation.getByLabel(last_word);
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

                    } else if(state == '/') {
                        // The string must be considering cur_amr and cur_amr must not have
                        // nodeValue[1] filled in yet.
                        cur_amr.nodeValue[1] = cur_charseq;
                        cur_amr.nodeType = AMR.AMRType.string;
                        cur_charseq = "";
                        state = '\0';
                    } else {
                        if(Logger.debug()) {
                            System.out.println("Error: String without a relation!");
                            System.out.println("Offending AMR: " + text);
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
                    SemanticRelation sr = SemanticRelation.getByLabel(last_word);
                    if(sr == null) {
                        if(Logger.debug()) {
                            System.out.println("Error: Unrecognized relation " + last_word  +
                                               " while parsing AMR!");
                        }
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
                        SemanticRelation sr = SemanticRelation.getByLabel(last_word);
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
                                                           cur_charseq, AMR.AMRType.string);
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
            System.out.println(AMRParser.convertTextToAMR(s));
            //AMR result = parseAMRString(s);
            //System.out.println(result.toString());

        }
    }

}
