package edu.pugetsound.mathcs.nlp.datag;

import java.util.*;
import java.io.*;

public class SwitchboardParser {
	/**
	* Given a path name, parses through the file and returns the list of DATags associated with each entry.
	**/
	public static List<DialogAct> parse(String path){
		File folder = new File("./nlp425/resources/swb1_dialogact_annot/sw00utt/");
		File[] files = folder.listFiles();
		System.out.println(files[0]);
		return null;
	}

	public static void main(String[] args){

	}
}