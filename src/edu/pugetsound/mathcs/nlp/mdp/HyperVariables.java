package edu.pugetsound.mathcs.nlp.mdp;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zachary Cohan
 */
 class HyperVariables {

    private static double GAMMA;
    private static int EXPLORE;
    
    public HyperVariables(double gamma, int explore)
    {
        GAMMA = gamma;
        EXPLORE = explore;
    }
    
    public double getGamma()
    {
        return GAMMA;
    }
    
    public void setGamma(double g)
    {
        GAMMA = g;
    }
    
    public int getExplore()
    {
        return EXPLORE;
    }
    
    public void setExplore(int e)
    {
        EXPLORE = e;
    }
    
    
}
