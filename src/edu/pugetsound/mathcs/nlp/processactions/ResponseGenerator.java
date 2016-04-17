package edu.pugetsound.mathcs.nlp.processactions;

import java.lang.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;


import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.datag.DAClassifier;
import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.processactions.MappingGenerator;
import edu.pugetsound.mathcs.nlp.features.TextAnalyzer;


//Requires Jython 2.5: http://www.jython.org/
//http://search.maven.org/remotecontent?filepath=org/python/jython-standalone/2.7.0/jython-standalone-2.7.0.jar
import org.python.util.PythonInterpreter;
import org.python.core.PyList;
import org.python.core.PyString;
import org.python.core.PyInteger;


import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;


/**
 * This script will read a text file of utterances and generate
 * responses from them for the processActions templates to use.
 * Class is only ment to be called for its main() when preloading json responses.
 *
 * Now, we can make this bot talk like Abe Lincoln or Darth Vader or Donald Trump!!!
 * Make NLP great again!
 *
 *
 *Usage: java -cp ClasspathToJars edu.pugetsound.mathcs.nlp.processactions.ResponseGenerator args inputFile.txt outputFile.txt
 *
 *  inputFile.txt: The exact path to the file to be used as input
 *  outputFile.txt: The name of the file to be writen to as output, within the processactions/srt folder. 
 *      Defaults to responses.json. If file exists, it is read from and responses are added to it.\n"
 *  Args:
 *    -h, --help: Display this message
 *
 * @author Jon Sims
 */
public class ResponseGenerator {


    private static String[] getUtterances(File f) throws FileNotFoundException,IOException {
        DocumentPreprocessor dp = new DocumentPreprocessor(
            new BufferedReader(
                new FileReader(f)));
        ArrayList<String> sentences = new ArrayList<String>();
        String sentence;
        for (List<HasWord> hw: dp){
            if (hw.size() > 0 && hw.size() < 26) {
                sentence = "";
                for (HasWord w: hw)
                    sentence+=w.word()+" ";
                sentences.add(sentence);
            }
        }
        String[] utts = new String[sentences.size()];
        return sentences.toArray(utts);
    }

    private static int saveUtterancesWithAnalysis(String[] utterances, String outfileName) {
        Conversation tempConvo;
        ArrayList<Utterance> convoList = new ArrayList<Utterance>();
        PyString[] tokens;
        TextAnalyzer ta = new TextAnalyzer();
        HashMap<DialogueActTag, String> daTagToTemplate = MappingGenerator.populateMappingDATags();
        PythonInterpreter python = new PythonInterpreter();
        python.execfile("../scripts/responseTemplater.py");            
        for (int p=0; p<utterances.length; p++) {
            try {
                verifyLists(python);

                if (p > 15 && (p % 15) == 0){
                    System.out.println("Writing current progress to file since we've analyzed "+p+" paragraphs.");
                    python.exec("main(fn)");
                }

                tempConvo = new Conversation();
                for (Utterance u: convoList)
                    tempConvo.addUtterance(u);
                Utterance currentUtt = ta.analyze(utterances[p], tempConvo);

                //currentUtt needs to be totally filled out with data at this point, or else thrown away
                if (currentUtt.daTag == null || currentUtt.amr == null || currentUtt.tokens == null || currentUtt.tokens.size() == 0) 
                    throw new Exception("There is some issue with current utterance:\n"
                        + "datag:"+currentUtt.daTag
                        + "\namr:" + currentUtt.amr
                        + "\ntokens: "+ currentUtt.tokens
                        + "\ntokens size: "+currentUtt.tokens.size());
                if ( ! daTagToTemplate.containsKey(currentUtt.daTag))
                    System.out.println("Unfortunately, the current daTag '"+currentUtt.daTag+"' isn't in our datagToTemplate mapping");
                else {
                    python.set("dat", new PyString(daTagToTemplate.get(currentUtt.daTag)));
                    python.exec("DATags.append(dat)");
                    python.set("amr", new PyString(currentUtt.amr.toString()));
                    python.exec("AMRs.append(amr)");

                    tokens = new PyString[currentUtt.tokens.size()];
                    for (int i=0; i<currentUtt.tokens.size(); i++)
                        tokens[i] = new PyString(currentUtt.tokens.get(i).token);
                    python.set("ts", new PyList(tokens));
                    python.exec("tokens.append(ts)");

                    convoList.add(currentUtt);
                    if (convoList.size() > 10)
                        convoList.remove(0);

                    System.out.println("Done asking the TextAnalyzer to analyze each utterance with an AMR/DATag/tokens.");
                }
            } catch(Exception e) {
                System.out.println("Issue with paragraph "+p+"; reverting any added amrs/utterances/datags to last paragraph.");
                System.out.println(e);
            }
        }
        python.exec("tokensLen = len(tokens)");
        int tokensLen = ((PyInteger) python.get("tokensLen")).asInt();
        System.out.println("Now writing "+tokensLen+"/"+utterances.length+" results to output file at "+outfileName);
        python.set("fn", new PyString(outfileName));
        python.exec("main(fn)");

        return tokensLen;
    }

    private static void verifyLists(PythonInterpreter python) {

        python.exec("tokensLen = len(tokens)");
        int tokensLen = ((PyInteger) python.get("tokensLen")).asInt();
        python.exec("amrLen = len(AMRs)");
        int amrLen = ((PyInteger) python.get("amrLen")).asInt();
        python.exec("daTagsLen = len(DATags)");
        int daTagsLen = ((PyInteger) python.get("daTagsLen")).asInt();
        
        if (tokensLen != amrLen)
            if (tokensLen > amrLen) {
                python.exec("utterances = utterances[:amrLen]");
                python.exec("tokensLen = len(tokens)");
                tokensLen = ((PyInteger) python.get("tokensLen")).asInt();
            }
            else {
                python.exec("AMRs = AMRs[:tokensLen]");
                python.exec("amrLen = len(AMRs)");
                amrLen = ((PyInteger) python.get("amrLen")).asInt();
            }
        if (tokensLen > daTagsLen){
            python.exec("utterances = utterances[:daTagsLen]");
            python.exec("tokensLen = len(tokens)");
            tokensLen = ((PyInteger) python.get("tokensLen")).asInt();
        }
        if (amrLen > daTagsLen){
            python.exec("AMRs = AMRs[:daTagsLen]");
            python.exec("amrLen = len(AMRs)");
            amrLen = ((PyInteger) python.get("amrLen")).asInt();
        }
    }



    public static void main(String a[]) {
        ArrayList<String> args = new ArrayList<String>(Arrays.asList(a));
        if (args.contains("-h") || args.contains("--help")) {
            System.out.println( "This script will read a text file of utterances and generate"
                +" responses from them for the processActions templates to use."
                +"Now, we can make this bot talk like Abe Lincoln or Darth Vader or Donald Trump!!!"
                +"Make NLP great again!\n\n"
                +"Usage:\n\tjava -cp ClasspathToJars edu.pugetsound.mathcs."
                +"nlp.processactions.ResponseGenerator args inputFile.txt outputFile.txt\n"
                +"inputFile.txt: The exact path to the file to be used as input\n"
                +"outputFile.txt: The name of the file to be writen to as output, within the "
                +"processactions/srt folder. Defaults to responses.json. If file exists, it is read "
                +"from and responses are added to it.\n"
                +"Args:\n\t-h, --help: Display this message" );
        } 
        else if (args.size() > 2 || args.size() == 0){
            System.out.println("Error with arguments: need one or two. You provided "+args.size());
        } 
        else {
            File inputFile = new File(args.get(0));
            if(!inputFile.exists() || inputFile.isDirectory()) {
                System.out.println("Error with first arg: not a valid file");
            } 
            else {
                if (args.size() == 1)
                    args.add("responses.json");

                try {

                    System.out.println("Reading from input file at "+inputFile.getAbsolutePath());
                    String[] utterances = getUtterances(inputFile);
                    System.out.println("Done reading from input file.");
                    int tokensLen = saveUtterancesWithAnalysis(utterances, "../src/edu/pugetsound/mathcs/nlp/processactions/srt/" + args.get(1));
                    if (tokensLen > 0) {
                        System.out.println("Done writing results to output file!");
                    }
                    else {
                        System.out.println("Warning: no utterances/AMRs found. Not writing anything to output file.");
                    }

                } catch(FileNotFoundException ex) {
                    System.out.println("Error: Unable to open file");    
                    ex.printStackTrace();            
                } catch (IOException e) {
                    System.out.println("Error: IOException");
                    e.printStackTrace();
                } 
            }
        }
    }       

}
