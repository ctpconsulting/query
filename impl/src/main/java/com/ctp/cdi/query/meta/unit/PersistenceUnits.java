package com.ctp.cdi.query.meta.unit;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
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
        EntityMapping entity = find(entityClass);
        if (entity != null) {
            return entity.getId();
        }
        return null;
    }
    
    public Class<?> primaryKeyIdClass(Class<?> entityClass) {
        try {
            EntityMapping entity = find(entityClass);
            if (entity != null && entity.getIdClass() != null) {
                return Class.forName(entity.getIdClass());
            }
            return null;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Failed to instantiate IdClass", e);
        }
    }
    
    public String entityName(Class<?> entityClass) {
        EntityMapping entity = find(entityClass);
        if (entity != null) {
            return entity.getName();
        }
        return null;
    }
    
    public DaoEntity lookupMetadata(Class<?> entityClass) {
        EntityMapping entity = find(entityClass);
        if (entity != null) {
            return new DaoEntity(entityClass, entity.idClass());
        }
        return null;
    }
    
    private List<PersistenceUnit> readPersistenceXmls() {
        try {
            List<PersistenceUnit> result = new LinkedList<PersistenceUnit>();
            Enumeration<URL> urls = classloader().getResources(PersistenceUnit.RESOURCE_PATH);
            while (urls.hasMoreElements()) {
                URL u = urls.nextElement();
                result.addAll(PersistenceUnit.readFromFile(u));
            }
            return Collections.unmodifiableList(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read persistence unit info", e);
        }
    }

    private ClassLoader classloader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    private EntityMapping find(Class<?> entityClass) {
        for (PersistenceUnit unit : persistenceUnits) {
            EntityMapping entity = unit.find(entityClass);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

}
