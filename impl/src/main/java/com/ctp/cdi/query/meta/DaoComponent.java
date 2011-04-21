package com.ctp.cdi.query.meta;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DaoComponent {

    private Class<?> daoClass;
    private DaoEntity entityClass;
    
    private Map<Method, DaoMethod> methods = new HashMap<Method, DaoMethod>();
    
    public DaoComponent(Class<?> daoClass, Collection<Class<?>> allImplemented, DaoEntity entityClass) {
        if (entityClass == null)
            throw new RuntimeException("Entity class extraction failed for DAO " + daoClass.getName());
        this.daoClass = daoClass;
        this.entityClass = entityClass;
        initialize(allImplemented);
    }
    
    public DaoMethod lookupMethod(Method method) {
        return methods.get(method);
    }
    
    public MethodType lookupMethodType(Method method) {
        return lookupMethod(method).getMethodType();
    }

    public Class<?> getEntityClass() {
        return entityClass.getEntityClass();
    }

    public Class<? extends Serializable> getPrimaryKey() {
        return entityClass.getPrimaryClass();
    }
    
    public Class<?> getDaoClass() {
        return daoClass;
    }

    private void initialize( Collection<Class<?>> allImplemented) {
        for (Class<?> implemented : allImplemented) {
            Method[] daoClassMethods = implemented.getDeclaredMethods();
            for (Method daoClassMethod : daoClassMethods) {
                DaoMethod daoMethod = new DaoMethod(daoClassMethod, this);
                methods.put(daoClassMethod, daoMethod);
            }
        }
    }

}
