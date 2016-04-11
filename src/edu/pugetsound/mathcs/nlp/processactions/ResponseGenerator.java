package edu.pugetsound.mathcs.nlp.processactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.datag.DAClassifier;
// import edu.pugetsound.mathcs.nlp.processactions.ExtendedDialogueActTag;
// import edu.pugetsound.mathcs.nlp.processactions.srt.*;


//Requires Jython 2.5: http://www.jython.org/
//http://search.maven.org/remotecontent?filepath=org/python/jython-standalone/2.7.0/jython-standalone-2.7.0.jar
import org.python.util.PythonInterpreter;
import org.python.core.PyList;
import org.python.core.PyString;
import org.python.core.PyInteger;


/**
 * The main response generator of the Process Actions step
 * This class should only be used to access the method generateResponse(...);
 * @author Thomas Gagne
 */
public class ResponseGenerator {


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
        } else if (args.size() > 2 || args.size() == 0){
            System.out.println("Error with arguments: need one or two. You provided "+args.size());
        } else {
            File inputFile = new File(args.get(0));
            if(!inputFile.exists() || inputFile.isDirectory()) {
                System.out.println("Error with first arg: not a valid file");
            } else {
                if (args.size() == 1)
                    args.add("responses.json");
                try {

                    System.out.println("Reading from input file at "+inputFile.getAbsolutePath());
                    FileReader fr = new FileReader(inputFile);
                    BufferedReader bufferedReader = new BufferedReader(fr);
                    ArrayList<String> paragraphs = new ArrayList<String>();
                    String paragraph = "";
                    String line;
                    while( (line = bufferedReader.readLine()) != null)
                        if (line.length() > 1)
                            paragraph += line.trim()+" ";
                        else if (paragraph.length() > 1) {
                            paragraphs.add(new String(paragraph));
                            paragraph = "";
                        }
                    bufferedReader.close(); 
                    System.out.println("Done reading from input file.");

                    Conversation utterances;
                    DAClassifier classifier;
                    PyString[] daTags;
                    PyList texts;
                    List<Utterance> convo;
                    int numTokens, utterancesLen;
                    PythonInterpreter python = new PythonInterpreter();
                    python.execfile("../scripts/responseTemplater.py");
                    for (int p=0; p<paragraphs.size(); p++) {
                        paragraph = paragraphs.get(p);
                        System.out.println("Now querying MSR SPLAT for AMR/Tokens for paragraph "+(p+1)+" of "+paragraphs.size());
                        
                        python.set("text", new PyString(paragraph)); //Watch out for "Cannot create PyString with non-byte value"
                        python.exec("numTokens = analyzeUtteranceString(text)");
                        numTokens = ((PyInteger) python.get("numTokens")).asInt();
                        python.exec("utterancesLen = len(utterances)");
                        utterancesLen = ((PyInteger) python.get("utterancesLen")).asInt();
                        System.out.println("Done querying MSR SPLAT for AMR/Tokens.\n"
                            +"Now asking the DAClassifier to classify each utterance with a DATag for paragraph "+(p+1)+" of "+paragraphs.size());

                        utterances = new Conversation();
                        classifier = new DAClassifier();
                        texts = (PyList) ((PyList) python.get("utterances")).subList(utterancesLen-numTokens, utterancesLen);
                        for (Object utt: texts) 
                            utterances.addUtterance(new Utterance((String) utt));
                        daTags = new PyString[utterances.getConversation().size()];
                        convo = utterances.getConversation();
                        for (int i=0; i<convo.size(); i++) {
                            convo.get(i).daTag = classifier.classify(convo.get(i), utterances);
                            daTags[i] = new PyString(convo.get(i).daTag.toString()); }
                        python.set("lst", new PyList(daTags));
                        python.exec("DATags += lst");
                        System.out.println("Done asking the DAClassifier to classify each utterance with a DATag.");
                    }
                    System.out.println("Now writing results to output file at "
                            +"../src/edu/pugetsound/mathcs/nlp/processactions/srt/" + args.get(1));
                    python.set("fn", new PyString("../src/edu/pugetsound/mathcs/nlp/processactions/srt/" + args.get(1)));
                    python.exec("main(fn)");
                    System.out.println("Done writing results to output file!");
                    

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
