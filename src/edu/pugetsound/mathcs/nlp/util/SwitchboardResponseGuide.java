package edu.pugetsound.mathcs.nlp.processactions;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
import java.lang.Double;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;


import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.lang.AMR;
import edu.pugetsound.mathcs.nlp.controller.Controller;

import edu.pugetsound.mathcs.nlp.lang.*;
import edu.pugetsound.mathcs.nlp.features.*;


public class SwitchboardResponseGuide{


    public static void main(String args[]) {

        TextAnalyzer ta = new TextAnalyzer();
        Conversation convo = new Conversation();
        HashMap<DialogueActTag, String> daTagToTemplate = MappingGenerator.hardcodedMap;

        BufferedReader bf = new BufferedReader(
            new FileReader(
                new File(
                    Controller.getBasePath()+"models/responses/swb_parsed.csv")));
        
        String previousLine = "";
        String currentline = "";
        String[] entry;
        TreeSet<String> resultsToWrite = new TreeSet<String>();
        String result = "";
        Utterance tempUtterance;

        while ((currentline = bf.readLine()) != null) {
            if (previousLine.length > 0) {
                if (currentline.length > 0) {
                    entry = previousLine.split(',');
                    tempUtterance = ta.analyze( entry[1], convo);
                    tempUtterance.datag = DialogueActTag.fromLabel(entry[0]);
                    convo.addUtterance(tempUtterance);
                    result = tempUtterance.datag.toString() + "\n  " + 
                        tempUtterance.datag.toString() + " \t" +
                        tempUtterance.utterance + "\t" + 
                        tempUtterance.amr.toString();
                    entry = currentLine.split(',');
                    tempUtterance = ta.analyze( entry[1], convo);
                    convo.getLastUtterance().datag = DialogueActTag.fromLabel(entry[0]);
                    convo.addUtterance(tempUtterance);
                    result = daTagToTemplate.get(tempUtterance.datag) + "_" + result +
                        daTagToTemplate.get(tempUtterance.datag) + " \t" +
                        tempUtterance.utterance + "\t" + 
                        tempUtterance.amr.toString() + "\n\n";
                    resultsToWrite.add(result);
                    result = "";
                } else {
                    convo = new Conversation();
                }
            }
            previousLine = currentline;
        }
        
    }


}