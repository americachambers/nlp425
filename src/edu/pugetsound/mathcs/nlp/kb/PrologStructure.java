package edu.pugetsound.mathcs.nlp.kb;

import java.util.Arrays;

/**
 * Represents a Prolog term or predicate
 * @author alchambers
 *
 */
public class PrologStructure {
	/**
	 * The name of the term or predicate
	 */
	private String name;
	
	/**
	 * If a predicate, this holds the arguments
	 */
	private String[] arguments;
		
	/**
	 * The arity
	 */
	private int arity;
	
	
	/**
	 * Constructs a Prolog term or predicate
	 * @param parity The parity of the predicate or zero for a term
	 * @throws IllegalArgumentException If parity is negative
	 */
	public PrologStructure(int arity) throws IllegalArgumentException{
		if(arity < 0){
			throw new IllegalArgumentException();
		}	
		this.arity = arity;
		name = null;
		arguments = (arity == 0) ? null : new String[arity];
	}
	
	
	/**
	 * Set the name of the predicate or term
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}

	
	public boolean isSet(int index) throws IllegalArgumentException, IllegalStateException {
		if(isTerm()){
			throw new IllegalStateException();
		}
		if(!validIndex(index)){
			throw new IllegalArgumentException();
		}		
		return arguments[index] != null;		
	}
	
	/**
	 * Add an argument to the predicate
	 * @param arg Predicate argument
	 * @param index Position of the argument in the predicate
	 * @throws IllegalArgumentException Invalid index
	 * @throws IllegalStateException Illegally calling method on a term
	 */
	public void addArgument(String arg, int index) throws IllegalArgumentException, IllegalStateException{
		if(isTerm()){
			throw new IllegalStateException();
		}
		if(!validIndex(index)){
			throw new IllegalArgumentException();
		}
		arguments[index] = arg;
	}
	
	/**
	 * Returns the arity
	 * @return zero for a term, a positive integer for a predicate 
	 */
	public int getArity(){
		return arity;
	}	
	
	/**
	 * Returns the name of the predicate
	 * @return The name of the predicate
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the specified argument
	 * @return The specified argument
	 * @throws IllegalStateException Calling method on a term
	 * @throws IllegalArgumentException Invalid index
	 */
	public String getArgument(int index) throws IllegalStateException, IllegalArgumentException {
		if(isTerm()){
			throw new IllegalStateException();
		}
		if(!validIndex(index)){
			throw new IllegalArgumentException();
		}
		return arguments[index];
	}
	
	/**
	 * Returns entire argument list
	 * @return list of arguments for predicate or null if term
	 */
	public String[] getArguments() {		
		return arguments;
	}
	
	/**
	 * Returns a string representation
	 */
	public String toString(){
		String toReturn = "";
		toReturn += name+"(";
		for(int i=0;i<arguments.length;i++){
			toReturn += arguments[i];
			if(i!=arguments.length-1){
				toReturn +=",";
			}
		}
		toReturn += ").";
		return toReturn;
//		if(isTerm()){
//			return name;
//		}
//		return name + Arrays.toString(arguments);
	}
	
	private boolean validIndex(int index){
		return index >= 0 && index < arity;
	}
	
	private boolean isTerm(){
		return arity == 0;
	}
}
