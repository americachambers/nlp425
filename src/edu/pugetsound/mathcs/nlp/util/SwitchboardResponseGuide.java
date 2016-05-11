//package edu.pugetsound.mathcs.nlp.util;
//
//import java.util.Arrays;
//import java.util.TreeSet;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.Collections;
//import java.util.Comparator;
//import java.lang.Math;
//import java.lang.Double;
//
//import java.io.File;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.FileNotFoundException;
//
//
//import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
//import edu.pugetsound.mathcs.nlp.lang.AMR;
//import edu.pugetsound.mathcs.nlp.controller.Controller;
//
//import edu.pugetsound.mathcs.nlp.lang.*;
//import edu.pugetsound.mathcs.nlp.features.*;
//import edu.pugetsound.mathcs.nlp.processactions.MappingGenerator;
//
//
//public class SwitchboardResponseGuide{
//
//
//    public static void main(String args[]) {
//
//        TextAnalyzer ta = new TextAnalyzer();
//        Conversation convo = new Conversation();
//        HashMap<DialogueActTag, String> daTagToTemplate = MappingGenerator.hardcodedMap;
//
//        BufferedReader bf = new BufferedReader(
//            new FileReader(
//                new File(
//                    Controller.getBasePath()+"models/responses/swb_parsed.csv")));
//
//        String previousLine = "";
//        String currentLine = "";
//        String[] entry;
//        TreeSet<String> resultsToWrite = new TreeSet<String>();
//        String result = "";
//        Utterance tempUtterance;
//
//        while ((currentLine = bf.readLine()) != null) {
//            if (previousLine.length() > 0) {
//                if (currentLine.length() > 0) {
//                    entry = previousLine.split(",");
//                    tempUtterance = ta.analyze( entry[1], convo);
//                    tempUtterance.daTag = DialogueActTag.fromLabel(entry[0]);
//                    convo.addUtterance(tempUtterance);
//                    result = tempUtterance.daTag.toString() + "\n  " +
//                        tempUtterance.daTag.toString() + " \t" +
//                        tempUtterance.utterance + "\t" +
//                        tempUtterance.amr.toString();
//                    entry = currentLine.split(",");
//                    tempUtterance = ta.analyze( entry[1], convo);
//                    convo.getLastUtterance().daTag = DialogueActTag.fromLabel(entry[0]);
//                    convo.addUtterance(tempUtterance);
//                    result = daTagToTemplate.get(tempUtterance.daTag) + "_" + result +
//                        daTagToTemplate.get(tempUtterance.daTag) + " \t" +
//                        tempUtterance.utterance + "\t" +
//                        tempUtterance.amr.toString() + "\n\n";
//                    resultsToWrite.add(result);
//                    result = "";
//                } else {
//                    convo = new Conversation();
//                }
//            }
//            previousLine = currentline;
//        }
//
//
//        BufferedWriter bw = new BufferedWriter(
//            new FileWriter(
//                new File(
//                    Controller.getBasePath()+"models/responses/guide.txt")));
//
//
//        for (String line: resultsToWrite)
//            bw.write(line);
//
//    }
//
//
//}
>>>>>>> master
