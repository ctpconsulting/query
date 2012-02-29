package com.ctp.cdi.query.handler;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ctp.cdi.query.meta.DaoMethod;
import com.ctp.cdi.query.param.Parameters;

public class QueryInvocationContext {

    private final EntityManager entityManager;
    private final Parameters params;
    private final InvocationContext invocation;
    private final Class<?> entityClass;
    private final DaoMethod daoMethod;
    
    public QueryInvocationContext(InvocationContext invocation, DaoMethod daoMethod, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.params = Parameters.create(invocation.getMethod(), invocation.getParameters());
        this.invocation = invocation;
        this.daoMethod = daoMethod;
        this.entityClass = daoMethod.getDao().getEntityClass();
    }
    
    public Object executeQuery(Query jpaQuery) {
        return daoMethod.getQueryProcessor().executeQuery(jpaQuery);
    }
    
    public Method getMethod() {
        return invocation.getMethod();
    }

    public Parameters getParams() {
        return params;
    }

    public InvocationContext getInvocation() {
        return invocation;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public DaoMethod getDaoMethod() {
        return daoMethod;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
