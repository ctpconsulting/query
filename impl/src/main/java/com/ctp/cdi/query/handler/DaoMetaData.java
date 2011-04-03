package com.ctp.cdi.query.handler;

import java.io.Serializable;

/**
 * Data structure to store information about a DAO.
 * @author thomashug
 */
public class DaoMetaData {

    private Class<?> entityClass;
    private Class<? extends Serializable> primaryClass;
    
    public DaoMetaData(Class<?> entityClass) {
        this(entityClass, null);
    }
    
    public DaoMetaData(Class<?> entityClass, Class<? extends Serializable> primaryClass) {
        this.entityClass = entityClass;
        this.primaryClass = primaryClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
    
    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }
    
    public Class<? extends Serializable> getPrimaryClass() {
        return primaryClass;
    }
    
    public void setPrimaryClass(Class<? extends Serializable> primaryClass) {
        this.primaryClass = primaryClass;
    }

}
