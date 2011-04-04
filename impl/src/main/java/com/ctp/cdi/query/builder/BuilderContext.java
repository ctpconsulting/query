/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctp.cdi.query.builder;

import com.ctp.cdi.query.param.Parameters;

/**
 *
 * @author thomashug
 */
public class BuilderContext {

    private final Parameters parameters;
    private int counter = 1;
    
    public BuilderContext(Parameters parameters) {
        this.parameters = parameters;
    }
    
    public int increment() {
        return counter++;
    }

    public int getCounter() {
        return counter;
    }

    public Parameters getParameters() {
        return parameters;
    }

}
