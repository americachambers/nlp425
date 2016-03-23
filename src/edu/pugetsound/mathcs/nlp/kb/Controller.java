import gnu.prolog.database.*;
import gnu.prolog.io.*;
import gnu.prolog.term.*;
import gnu.prolog.vm.*;
import gnu.prolog.vm.buildins.database.*;

public class Controller{

  Environment env = new Environment();
  env.ensureLoaded(AtomTerm.get(Test.class.getResource("knowledge/cats.pl").getFile()));
  Interpreter interpreter = env.createInterpreter();
  env.runInitialization(interpreter);


/*
* User called yes/no query method
* @param pred   predicate being queried
* @param args   any predicate fields for that predicate
* @return   true if query successful, false if not
*/
  public boolean yesNo(String pred, String[] args){
    //TODO eventually add code to pick which interpreter to use (for now only query cat file)
    Interpreter interpret = interpreter;




    int rc = runQuery(interpret, pred,args);
    if (rc == PrologCode.SUCCESS || rc == PrologCode.SUCCESS_LAST){
      return true;
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
}

//method handling knowledge assertion using specific interpreter
private static int assertKnowledge(Interpreter interpreter, String pred, String[] assertArgs) throws PrologException{
  //TODO this method doesn't actually work... fix it

  Term[] terms = new Term[assertArgs.length+1];
  terms[0] = AtomTerm.get(pred);
  for(int i=0;i<assertArgs.length;){
    terms[i+1] = AtomTerm.get(assertArgs[i]);
  }
  Predicate_assert pred = new Predicate_assertz();
  int rc = pred.execute(interpreter,false,terms);
  return rc;//returns whether successful
}
