//package edu.pugetsound.mathcs.nlp.datag;

import java.util.*;
import java.io.*;

public class SwitchboardParser {
	/**
	* Given a path name, parses through the file and returns the list of DATags associated with each entry.
	**/
	public static List<DialogueAct> parse(String path){
		File folder = new File(path);
		File[] files = folder.listFiles();
		//System.out.println(files[0]);
		Scanner sc;
		for (File file : files){
			sc = new Scanner(file);


			sc.close();
		}

		return null;
	}



	public static void main(String[] args){
		parse("");
	}
}