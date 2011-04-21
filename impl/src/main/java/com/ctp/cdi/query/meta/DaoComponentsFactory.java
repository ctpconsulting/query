package com.ctp.cdi.query.meta;

import javax.enterprise.inject.Produces;

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
