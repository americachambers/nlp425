package edu.pugetsound.mathcs.nlp.kb;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;


/**
 * This class provides a single static method that allows the user
 * to append PrologStructure objects to a File with the given filename.
 */
public class Writer {

	/**
	 * Writes the List of ProloStructure objects to a File with the
	 * given name, if it exists.
	 * 
	 * @param filename The name of a file to write to.
	 * @param list     The list of ProloStructure objects to be written to file.
	 */
	public static void write(String filename, List<PrologStructure> list) {

		File file = new File(filename);

		try {
			if (!file.exists()) {
				System.out.println("The file you wish to write to does not exist.");
				return;
			}

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("\n");
			
			for (PrologStructure struct : list) {
				String strRep = struct.toString();
				bw.write(strRep + "\n");
			}
			bw.close();
		} catch(IOException e) {
			System.out.println("Failed to write to file.\n");
			e.printStackTrace();
		}
	}

}