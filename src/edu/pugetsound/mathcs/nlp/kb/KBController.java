package edu.pugetsound.mathcs.nlp.kb;

import java.util.ArrayList;
import java.util.List;

import gnu.prolog.term.AtomTerm;
import gnu.prolog.term.CompoundTerm;
import gnu.prolog.term.Term;
import gnu.prolog.vm.Environment;
import gnu.prolog.vm.Interpreter;
import gnu.prolog.vm.PrologCode;
import gnu.prolog.vm.PrologException;
import gnu.prolog.vm.buildins.database.Predicate_assert;
import gnu.prolog.vm.buildins.database.Predicate_assertz;

public class KBController{
  private Environment env;
  //private Interpreter interpreter;

  /**
   * Constructs controller to knowledge base
   */
  public KBController(){
	env = new Environment();
    env.ensureLoaded(AtomTerm.get(KBController.class.getResource("knowledge/cats.pl").getFile()));
   // Interpreter interpreter = env.createInterpreter();
   // env.runInitialization(interpreter); //necessary?
  }

/**
* User called yes/no query method
* @param structs   list of predicates being queried
* @return   true if all queries successful, false if not
*/
  public boolean yesNo(List<PrologStructure> structs){
    //TODO eventually add code to pick which interpreter to use (for now only query cat file)
	 // env = interpreter.getEnvironment();
	  
	  //Interpreter interpret = env.createInterpreter();

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
  
//  /**
//   * Processes wh-questions to Prolog Database (this does not work yet)
//   * @param struct prolog predicate being queried
//   * @return all possible true predicates
//   */
//  public List<PrologStructure> query(PrologStructure struct){
//	  return queryHelp(env.createInterpreter(),struct.getName(),struct.getArguments());
//  }
//  
//  //helper for wh-questions
//  private static List<PrologStructure> queryHelp(Interpreter interpreter, String pred, String[] queryArgs){
//	  Term[] terms = new Term[queryArgs.length];
//	    for(int i=0;i<queryArgs.length;i++){
//	      terms[i] = AtomTerm.get(queryArgs[i]);
//	    }
//	  //  CompoundTerm goalTerm = new CompoundTerm(AtomTerm.get(pred), terms);
//	   // int rc = interpreter.runOnce(goalTerm);
//	  return null;
//  }
  
  //tester code for debugging knowledge assertion (when it works, should return true, true)
  public static void main(String[] args){
	  KBController kb = new KBController();
	  PrologStructure p = new PrologStructure(2);
	  List<PrologStructure> preds = new ArrayList<PrologStructure>();
	  preds.add(p);
	  p.setName("isA");
	  p.addArgument("fluffy",0);
	  p.addArgument("cat",1);
	  System.out.println("Answer: "+ kb.assertNew(preds));
	  System.out.println("Answer: " + kb.yesNo(preds));
  }
  
}
