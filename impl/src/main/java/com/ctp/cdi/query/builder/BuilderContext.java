/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctp.cdi.query.builder;

/**
 *
 * @author thomashug
 */
public class BuilderContext {
    
    private int counter = 1;
    
    public int increment() {
        return counter++;
    }

    public int getCounter() {
        return counter;
    }
    
}
