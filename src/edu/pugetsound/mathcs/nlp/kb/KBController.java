package edu.pugetsound.mathcs.nlp.kb;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.pugetsound.mathcs.nlp.util.PathFormat;
import gnu.prolog.term.AtomTerm;
import gnu.prolog.term.CompoundTerm;
import gnu.prolog.term.Term;
import gnu.prolog.term.VariableTerm;
import gnu.prolog.vm.Environment;
import gnu.prolog.vm.Interpreter;
import gnu.prolog.vm.TermConstants;
import gnu.prolog.vm.PrologCode;
import gnu.prolog.vm.PrologException;

public class KBController{
  private static Environment env;//file Prolog environment
  private Interpreter interpreter;//utility to talk to current file
  private String prologFile;//file being queried - eventually add a way to handle multiple files
  
  /**
   * Constructs controller to knowledge base
   * @param filename The Prolog file name to be initially used for query and assertion
   */
  public KBController(String filename){
	  //TODO save original file and use copy as working file to revert back to original working Prolog file
	 updateEnvironment(filename);
  }


/**
 * Updates the current environment to now query and process 
 * @param filename The new Prolog file to be initially used for query and assertion
 */
  public void updateEnvironment(String filename){
	  prologFile = filename;
	  env = new Environment();
	  env.ensureLoaded(AtomTerm.get(KBController.class.getResource(filename).getFile()));
	  interpreter = env.createInterpreter();
	  env.runInitialization(interpreter); //don't know if this is actually necessary?
  }
  
  
/**
* User called yes/no query method
* @param structs   list of predicates being queried
* @return   true if all queries successful, false if not
*/
  public boolean yesNo(List<PrologStructure> structs){
    //TODO eventually add code to pick which interpreter to use (for now only query cat file)

	  for(PrologStructure struct : structs){
		  try{
			  int rc = runQuery(interpreter, struct.getName(),struct.getArguments());

			  if (rc == PrologCode.SUCCESS || rc == PrologCode.SUCCESS_LAST){
				  continue;
			  }
			  else return false;
		  }
		  catch(PrologException e){
			  //TODO properly catch this
		  }
	  }
  	return true;
  }

  //method handling queries internally using a specific interpreter (picking which file to query)
  private static int runQuery(Interpreter interpreter, String pred, String[] queryArgs)  throws PrologException {
    Term[] terms = new Term[queryArgs.length];
    for(int i=0;i<queryArgs.length;i++){
      terms[i] = AtomTerm.get(queryArgs[i]);
    }
    CompoundTerm goalTerm = new CompoundTerm(AtomTerm.get(pred), terms);
    Term[] allTerms = new Term[1];
    allTerms[0] = goalTerm;
    
    int rc = interpreter.runOnce(goalTerm);
    return rc;
  }


  /**
   * Asserts new predicates to the database by writing to the end of the opened Prolog file.
   * @return Whether the file write was successful
   */
  public boolean assertNew(List<PrologStructure> structs){
	  //TODO eventually pick different files when our database grows
	  String filename = PathFormat.absolutePathFromRoot("src/edu/pugetsound/mathcs/nlp/kb/"+prologFile);
	  File file = new File(filename);
		try {
			if (!file.exists()) {
				System.out.println("The file "+filename+" you wish to write to does not exist.");
				return false;
			}

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("\n");
			for (PrologStructure struct : structs) {
				String strRep = struct.toString();
				bw.write(strRep + "\n");
			}
			bw.close();
		} catch(IOException e) {
			System.out.println("Failed to write to file.\n");
			e.printStackTrace();
		}
	  updateEnvironment(prologFile);
	  
	  return true;
  }

  /**
   * Processes wh-questions to Prolog Database (this does not work yet)
   * @param struct prolog predicate being queried
   * @return all possible true predicates
   */
  public List<PrologStructure> query(PrologStructure struct){
	  try{
		  queryHelp(env.createInterpreter(),struct.getName(),struct.getArguments());
		  
	  }
	  catch(PrologException e){
		  //TODO properly catch this
	  }
	  return null;
  }

  //helper for wh-questions that deals with creation of Terms and passing to interpreter
  private static List<PrologStructure> queryHelp(Interpreter interpreter, String pred, String[] queryArgs) throws PrologException{
	  
	  //TODO figure out proper implementation of Prolog backtracking with this silly library...
	  
	  
//	  VariableTerm listTerm = new VariableTerm("List");
//		// Create the arguments to the compound term which is the question
//		Term[] args = { new IntegerTerm(5), new IntegerTerm(5), listTerm, answerTerm };
//		// Construct the question
//		CompoundTerm goalTerm = new CompoundTerm(AtomTerm.get(pred+"List"), args);
//	  
//		int rc = interpreter.runOnce(goalTerm);
//
//	  
	  
	  Term[] terms = new Term[queryArgs.length];
	  int mark = 0;//list index marker
	    for(int i=0;i<queryArgs.length;i++){
	    	if(!Character.isUpperCase(queryArgs[i].charAt(0))){
	    		terms[i] = AtomTerm.get(queryArgs[i]);
  	    	}
	    	else{
	    		terms[i] = new VariableTerm("List");
	    		mark=i;
	    	}
	    }
	    CompoundTerm goalTerm = new CompoundTerm(AtomTerm.get(pred+"List"), terms);
	   
	   interpreter.runOnce(goalTerm);

	    Term list = terms[mark].dereference();
	    List<PrologStructure> answer = new ArrayList<PrologStructure>();
	    if (list != null)
		{
			if (list instanceof CompoundTerm)//found answers to the query
			{
				CompoundTerm cList = (CompoundTerm) list;
				if (cList.tag == TermConstants.listTag){
					//TODO figure out proper dereferencing of list terms to add to answer list to return
					//System.out.println(TermWriter.toString(list));
				}
			}
		}
	  return answer;
  }

  /**
   * Tester code to demonstrate yesNo(), assertNew(), and (eventually) query() methods
   * @param args None
   */
  public static void main(String[] args){
	  String filename = "knowledge/cats.pl";
	  KBController kb = new KBController(filename);
	  List<PrologStructure> preds = new ArrayList<PrologStructure>();
	  
	  PrologStructure pFalse = new PrologStructure(2);
	  pFalse.setName("isA");
	  pFalse.addArgument("josh",0);
	  pFalse.addArgument("dog",1);
	  
	  PrologStructure pTrue = new PrologStructure(2);
	  pTrue.setName("isA");
	  pTrue.addArgument("fluffy",0);
	  pTrue.addArgument("cat",1);
	  
	  PrologStructure pQuery = new PrologStructure(2);
	  pQuery.setName("isA");
	  pQuery.addArgument("fluffy",0);
	  pQuery.addArgument("X",1);
	  
	  preds.add(pTrue);
	  
	  System.out.println("Querying database for "+pTrue);
	  System.out.println("Expected: true\tActual: "+kb.yesNo(preds)+"\n");
	  
	  preds.remove(0);
	  preds.add(pFalse);
	  
	  System.out.println("Querying database for "+pFalse);
	  System.out.println("Expected: false\tActual: "+kb.yesNo(preds)+"\n");
	  
	  System.out.println("Writing "+pFalse+" to database");
	  System.out.println("Expected: true\tActual: "+kb.assertNew(preds)+"\n");
	  
	  System.out.println("Querying database for "+pFalse);
	  System.out.println("Expected: true\tActual: "+kb.yesNo(preds)+"\n");
	  
	  
	  //Eventually this should work if WH- questions are working but not yet...
//	  System.out.println("Querying database for "+pQuery);
//	  System.out.println("Expected: 3\tActual: "+kb.query(pQuery).size()+"\n");  
  }

}
