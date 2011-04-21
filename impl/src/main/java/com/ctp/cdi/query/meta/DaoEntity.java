package com.ctp.cdi.query.meta;

import java.io.Serializable;

/**
 * Data structure to store information about a DAO.
 * @author thomashug
 */
public class DaoEntity {

    private Class<?> entityClass;
    private Class<? extends Serializable> primaryClass;
    
    public DaoEntity(Class<?> entityClass) {
        this(entityClass, null);
    }
    
    public DaoEntity(Class<?> entityClass, Class<? extends Serializable> primaryClass) {
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
