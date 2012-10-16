package com.ctp.cdi.query.spi;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

public interface QueryInvocationContext {

    EntityManager getEntityManager();
    
    Method getMethod();
    
    Object[] getMethodParameters();
    
    Class<?> getEntityClass();

}
