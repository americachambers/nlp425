package edu.pugetsound.mathcs.nlp.kb;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import gnu.prolog.term.AtomTerm;
import gnu.prolog.term.CompoundTerm;
import gnu.prolog.term.IntegerTerm;
import gnu.prolog.term.Term;
import gnu.prolog.term.VariableTerm;
import gnu.prolog.vm.Environment;
import gnu.prolog.vm.ExecuteOnlyCode;
import gnu.prolog.vm.Interpreter;
import gnu.prolog.vm.Interpreter.Goal;
import gnu.prolog.vm.TermConstants;
import gnu.prolog.demo.mentalarithmetic.*;
import gnu.prolog.io.TermWriter;
import gnu.prolog.vm.PrologCode;
import gnu.prolog.vm.PrologException;
import gnu.prolog.vm.buildins.database.Predicate_assert;
import gnu.prolog.vm.buildins.database.Predicate_asserta;
import gnu.prolog.vm.buildins.database.Predicate_assertz;

public class KBController{
  private static Environment env;
  //private Interpreter interpreter;
  private String prologFile;
  
  /**
   * Constructs controller to knowledge base
   */
  public KBController(String filename){
	  prologFile = filename;
	env = new Environment();
    env.ensureLoaded(AtomTerm.get(KBController.class.getResource(filename).getFile()));
   // Interpreter interpreter = env.createInterpreter();
   // env.runInitialization(interpreter); //necessary?
  }


  //takes in new filename to use as main Prolog file
  public void updateEnvironment(String filename){
	  env.ensureLoaded(AtomTerm.get(KBController.class.getResource(filename).getFile()));
  }
  
/**
* User called yes/no query method
* @param structs   list of predicates being queried
* @return   true if all queries successful, false if not
*/
  public boolean yesNo(List<PrologStructure> structs){
    //TODO eventually add code to pick which interpreter to use (for now only query cat file)
	 // env = interpreter.getEnvironment();

	  for(PrologStructure struct : structs){
		  try{
			  int rc = runQuery(env.createInterpreter(), struct.getName(),struct.getArguments());

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
    Predicate_assert asserter = new Predicate_asserta();
    //int rc = asserter.execute(interpreter, true, allTerms);
    
    int rc = interpreter.runOnce(goalTerm);
    
    return rc;
  }

  //method handling knowledge assertion using specific interpreter
  private static int assertKnowledge(Interpreter interpreter, String pred, String[] assertArgs) throws PrologException{
    //TODO this method doesn't actually work... fix it

    Term[] terms = new Term[assertArgs.length+1];
    terms[0] = AtomTerm.get(pred);
    for(int i=0;i<assertArgs.length;i++){
      terms[i] = AtomTerm.get(assertArgs[i]);
    }
    Predicate_assert asserter = new Predicate_assertz();
    int rc = asserter.execute(interpreter,true,terms);
    return rc;//returns whether successful
  }

  public boolean assertNew(List<PrologStructure> structs){
    //TODO eventually add code to pick which interpreter to use (for now only cat file)
	 // Interpreter interpret = interpreter;
	  	for(PrologStructure struct : structs){
	  		try{
	  			int rc = assertKnowledge(env.createInterpreter(), struct.getName(),struct.getArguments());
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

  


<<<<<<< HEAD
=======
    for (PrologStructure ps : structs) {
      try {
        String s = ps.toString();
        byte[] sbytes = s.getBytes();
        strm.write(sbytes);
        strm.flush();

    
      }
      catch (IOException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
      }
    }
  }
>>>>>>> 0afebdd0458629084025032db4da7bcd549f25f8


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

  //helper for wh-questions
  private static List<PrologStructure> queryHelp(Interpreter interpreter, String pred, String[] queryArgs) throws PrologException{
//	  VariableTerm listTerm = new VariableTerm("List");
//		// Create the arguments to the compound term which is the question
//		Term[] args = { new IntegerTerm(5), new IntegerTerm(5), listTerm, answerTerm };
//		// Construct the question
//		CompoundTerm goalTerm = new CompoundTerm(AtomTerm.get(pred+"List"), args);
//	  
//		int rc = interpreter.runOnce(goalTerm);
//
//	  
	  
	  Term[] terms = new Term[queryArgs.length+1];
	    for(int i=0;i<queryArgs.length;i++){
	      terms[i] = AtomTerm.get(queryArgs[i]);
	    }
	    terms[queryArgs.length-1] = new VariableTerm("List");
	    CompoundTerm goalTerm = new CompoundTerm(AtomTerm.get(pred+"List"), terms);
	   
	    int rc = interpreter.runOnce(goalTerm);

	    Term list = terms[terms.length-1].dereference();
	    
	    System.out.println("ran");
	    if (list != null)
		{
	    	System.out.println("not null");
			if (list instanceof CompoundTerm)
			{
				CompoundTerm cList = (CompoundTerm) list;
				if (cList.tag == TermConstants.listTag)// it is a list
				{// Turn it into a string to use.
					System.out.println(TermWriter.toString(list));
				}
			}
		}
	  return null;
  }

  //tester code for debugging knowledge assertion (when it works, should return true, true)
  public static void main(String[] args){
	  String filename = "knowledge/cats.pl";
	  KBController kb = new KBController(filename);
	  
	  PrologStructure p = new PrologStructure(2);
	  List<PrologStructure> preds = new ArrayList<PrologStructure>();
	  p.setName("isA");
	  p.addArgument("fluffy",0);
	  p.addArgument("dog",1);
	  preds.add(p);
	  
	  kb.query(p);
	  
	//  kb.writeToDB(filename, preds);
	  
//	  System.out.println(kb.yesNo(preds));
//
//	  System.out.println("Answer: "+ kb.assertNew(preds));
//	  System.out.println("Answer: " + kb.yesNo(preds));
	  
  }

}
