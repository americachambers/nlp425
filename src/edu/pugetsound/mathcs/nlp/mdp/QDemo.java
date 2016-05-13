/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pugetsound.mathcs.nlp.mdp;

import edu.pugetsound.mathcs.nlp.datag.DialogueActTag;
import edu.pugetsound.mathcs.nlp.processactions.ResponseTag;
import java.util.EnumSet;
import java.util.ArrayList;
import edu.pugetsound.mathcs.nlp.lang.Conversation;
import edu.pugetsound.mathcs.nlp.lang.Utterance;
import edu.pugetsound.mathcs.nlp.mdp.HyperVariables;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Zack
 */
public class QDemo {

    protected static final double GAMMA = 0.1;
    protected static final int EXPLORE = 100000;
    protected static HyperVariables hyperVariables = new HyperVariables(GAMMA, EXPLORE);
    
    protected static EnumSet<DialogueActTag> limitedDATags = EnumSet.of(
        DialogueActTag.NULL,
        DialogueActTag.QUESTION_YES_NO,
        DialogueActTag.QUESTION_WH,
        DialogueActTag.CONVENTIONAL_OPENING,
        DialogueActTag.CONVENTIONAL_CLOSING);
    
    
    protected static EnumSet<ResponseTag> limitedResponseTags = EnumSet.of(
        ResponseTag.STATEMENT,
        ResponseTag.YES_NO_ANSWER,
        ResponseTag.CONVENTIONAL_OPENING,
        ResponseTag.CONVENTIONAL_CLOSING);
    
    public static void main(String[] args){
        Utterance utt1 = new Utterance("");
        utt1.daTag = DialogueActTag.QUESTION_YES_NO;
        Utterance utt2 = new Utterance("");
        utt2.daTag = DialogueActTag.QUESTION_WH;
        Utterance utt3 = new Utterance("");
        utt3.daTag = DialogueActTag.CONVENTIONAL_OPENING;
        Utterance utt4 = new Utterance("");
        utt4.daTag = DialogueActTag.CONVENTIONAL_CLOSING;
        Utterance utt5 = new Utterance("");
        utt5.daTag = DialogueActTag.QUESTION_YES_NO;
        
        Utterance[] list = {utt1,utt2,utt3,utt4};
        Conversation conversation = new Conversation();
        conversation.addUtterance(new Utterance(""));
        QLearner mdp = new QLearner(hyperVariables,false);

        for (int i =0; i<=100000; i++){
            Random r = new Random();
            int randomNumber = r.nextInt(4);
            conversation.addUtterance(list[randomNumber]);
            Action a = mdp.train(conversation);
            Utterance response = new Utterance("");
            conversation.addUtterance(response);  
        } 
        
        mdp.printPolicy();
    }
    
    public static int getResponseValue(State s, Action a){
        if(s.DATag2 == DialogueActTag.CONVENTIONAL_OPENING && a.DATag == ResponseTag.CONVENTIONAL_OPENING)
            return 2;
        else if (s.DATag2 == DialogueActTag.CONVENTIONAL_CLOSING && a.DATag == ResponseTag.CONVENTIONAL_CLOSING)
            return 2;
        else if (s.DATag2 == DialogueActTag.QUESTION_YES_NO && a.DATag == ResponseTag.YES_NO_ANSWER)
            return 2;
        else if(s.DATag2 == DialogueActTag.QUESTION_WH && a.DATag == ResponseTag.STATEMENT)
            return 2;
        else if(s.DATag2 == DialogueActTag.NULL && a.DATag == ResponseTag.CONVENTIONAL_OPENING)
            return 2;
        else
            return -2;   
    }
    
    
}
