package com.ctp.cdi.query.meta.unit;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.ctp.cdi.query.meta.DaoEntity;

public class PersistenceUnits {

    private static PersistenceUnits instance = new PersistenceUnits();
    
    private List<PersistenceUnit> persistenceUnits = Collections.emptyList();
    
    private PersistenceUnits() {
    }
    
    public static PersistenceUnits instance() {
        return instance;
    }
    
    public void init() {
        persistenceUnits = readPersistenceXmls();
    }

    public boolean isEntity(Class<?> entityClass) {
        return find(entityClass) != null;
    }
    
    public String primaryKeyField(Class<?> entityClass) {
        EntityDescriptor entity = find(entityClass);
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }
    
    public Class<?> primaryKeyIdClass(Class<?> entityClass) {
        EntityDescriptor entity = find(entityClass);
        if (entity != null && entity.getIdClass() != null) {
            return entity.getIdClass();
        }
        return null;
    }
    
    public String entityName(Class<?> entityClass) {
        EntityDescriptor entity = find(entityClass);
        if (entity != null) {
            return entity.getName();
        }
        return null;
    }
    
    public DaoEntity lookupMetadata(Class<?> entityClass) {
        EntityDescriptor entity = find(entityClass);
        if (entity != null) {
            return new DaoEntity(entityClass, entity.getIdClass());
        }
        return null;
    }
    
    private List<PersistenceUnit> readPersistenceXmls() {
        try {
            PersistenceUnitReader reader = new PersistenceUnitReader();
            return reader.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read persistence unit info", e);
        }
    }
    
    private EntityDescriptor find(Class<?> entityClass) {
        for (PersistenceUnit unit : persistenceUnits) {
            EntityDescriptor entity = unit.find(entityClass);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

}
