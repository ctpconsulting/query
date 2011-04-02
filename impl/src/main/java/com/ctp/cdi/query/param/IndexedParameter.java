package com.ctp.cdi.query.param;

import javax.persistence.Query;

/**
 * Query parameters which have an index (?1).
 * @author thomashug
 */
public class IndexedParameter extends Parameter {
    
    private final int index;

    public IndexedParameter(int index, Object value, int argIndex) {
        super(value, argIndex);
        this.index = index;
    }

    @Override
    public void apply(Query query) {
        query.setParameter(index, value);
    }
    
}
