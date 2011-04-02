package com.ctp.cdi.query.param;

import javax.persistence.Query;

/**
 * Parameters which have a name (:name).
 * @author thomashug
 */
public class NamedParameter extends Parameter {

    private String name;

    public NamedParameter(String name, Object value, int argIndex) {
        super(value, argIndex);
        this.name = name;
    }

    @Override
    public void apply(Query query) {
        query.setParameter(name, value);
    }
    
}
