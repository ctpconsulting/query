package com.ctp.cdi.query.handler;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ctp.cdi.query.meta.DaoMethod;
import com.ctp.cdi.query.param.Parameters;
import com.ctp.cdi.query.spi.QueryInvocationContext;

public class CdiQueryInvocationContext implements QueryInvocationContext {

    private final EntityManager entityManager;
    private final Parameters params;
    private final InvocationContext invocation;
    private final Class<?> entityClass;
    private final DaoMethod daoMethod;
    private final List<QueryStringPostProcessor> queryPostProcessors;
    private final List<JpaQueryPostProcessor> jpaPostProcessors;
    
    private String queryString;
    
    public CdiQueryInvocationContext(InvocationContext invocation, DaoMethod daoMethod, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.params = Parameters.create(invocation.getMethod(), invocation.getParameters());
        this.invocation = invocation;
        this.daoMethod = daoMethod;
        this.entityClass = daoMethod.getDao().getEntityClass();
        this.queryPostProcessors = new LinkedList<QueryStringPostProcessor>();
        this.jpaPostProcessors = new LinkedList<JpaQueryPostProcessor>();
    }
    
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    @Override
    public Method getMethod() {
        return invocation.getMethod();
    }
    
    @Override
    public Object[] getMethodParameters() {
        return invocation.getParameters();
    }
    
    @Override
    public Class<?> getEntityClass() {
        return entityClass;
    }
    
    public void addQueryStringPostProcessor(QueryStringPostProcessor postProcessor) {
        queryPostProcessors.add(postProcessor);
    }
    
    public void addJpaQueryPostProcessor(JpaQueryPostProcessor postProcessor) {
        jpaPostProcessors.add(postProcessor);
    }
    
    public void removeJpaQueryPostProcessor(JpaQueryPostProcessor postProcessor) {
        jpaPostProcessors.remove(postProcessor);
    }
    
    public boolean hasQueryStringPostProcessors() {
        return !queryPostProcessors.isEmpty();
    }
    
    public String applyQueryStringPostProcessors(String queryString) {
        String result = queryString;
        for (QueryStringPostProcessor processor : queryPostProcessors) {
            result = processor.postProcess(result);
        }
        return result;
    }
    
    public Query applyJpaQueryPostProcessors(Query query) {
        Query result = query;
        for (JpaQueryPostProcessor processor : jpaPostProcessors) {
            result = processor.postProcess(this, result);
        }
        return result;
    }
    
    public Object executeQuery(Query jpaQuery) {
        return daoMethod.getQueryProcessor().executeQuery(jpaQuery);
    }

    public Parameters getParams() {
        return params;
    }

    public InvocationContext getInvocation() {
        return invocation;
    }

    public DaoMethod getDaoMethod() {
        return daoMethod;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
    public List<QueryStringPostProcessor> getQueryStringPostProcessors() {
        return queryPostProcessors;
    }

}
