package edu.pugetsound.mathcs.nlp.kb;

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
  private Interpreter interpreter;

  /**
   * Constructs controller to knowledge base
   */
  public KBController(){
    env = new Environment();
    env.ensureLoaded(AtomTerm.get(KBController.class.getResource("knowledge/cats.pl").getFile()));
    interpreter = env.createInterpreter();
    env.runInitialization(interpreter);
  }

/**
* User called yes/no query method
* @param pred   predicate being queried
* @param args   any predicate fields for that predicate
* @return   true if query successful, false if not
*/
  public boolean yesNo(PrologStructure struct){
    //TODO eventually add code to pick which interpreter to use (for now only query cat file)
    Interpreter interpret = interpreter;

    String pred = struct.getName();
    int arity = struct.getArity();
    String[] args = new String[arity];
    for(int i=0;i<arity;i++){
        args[i]=struct.getArgument(i);
    }
    try{
      int rc = runQuery(interpret, pred,args);
      if (rc == PrologCode.SUCCESS || rc == PrologCode.SUCCESS_LAST){
        return true;
      }
      return false;
    }
    catch(PrologException e){
      //TODO properly catch this
    }
    return false;
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
    for(int i=0;i<assertArgs.length;){
      terms[i+1] = AtomTerm.get(assertArgs[i]);
    }
    Predicate_assert asserter = new Predicate_assertz();
    int rc = asserter.execute(interpreter,false,terms);
    return rc;//returns whether successful
  }
}
