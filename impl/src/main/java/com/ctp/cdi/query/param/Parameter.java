package com.ctp.cdi.query.param;

import javax.persistence.Query;

/**
 * Base class for parameters.
 * @author thomashug
 */
public abstract class Parameter {
    
    final Object value;
    final int argIndex;

    public Parameter(Object value, int argIndex) {
        this.value = value;
        this.argIndex = argIndex;
    }

    public abstract void apply(Query query);

}
