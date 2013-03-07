package org.apache.deltaspike.query.impl.meta;

import javax.enterprise.inject.Produces;

/**
 * DAO components producer. Exposes a singleton both programmatically
 * as well as over a CDI producer.
 * 
 * @author thomashug
 */
public class DaoComponentsFactory {

    private static DaoComponents components = new DaoComponents();
    
    public static DaoComponents instance() {
        return components;
    }
    
    @Produces @Initialized
    public DaoComponents producer() {
        return instance();
    }

}
