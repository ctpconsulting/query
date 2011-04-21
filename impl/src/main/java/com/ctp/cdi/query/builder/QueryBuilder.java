package com.ctp.cdi.query.builder;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.Query;

import com.ctp.cdi.query.handler.QueryInvocationContext;
import com.ctp.cdi.query.param.Parameters;

/**
 * Query builder factory. Delegates to concrete implementations.
 * 
 * @author thomashug
 */
public abstract class QueryBuilder {
    
    public static final String QUERY_SELECT = "select e from {0} e";
    public static final String QUERY_COUNT = "select count(e) from {0} e";
    public static final String ENTITY_NAME = "e";
    
    public static String selectQuery(String entityName) {
        return MessageFormat.format(QUERY_SELECT, entityName);
    }
    
    public static String countQuery(String entityName) {
        return MessageFormat.format(QUERY_COUNT, entityName);
    }
    
    public abstract Object execute(QueryInvocationContext ctx);
    
    protected boolean returnsList(QueryInvocationContext ctx) {
        return ctx.getMethod().getReturnType().isAssignableFrom(List.class);
    }
    
    protected LockModeType extractLockMode(QueryInvocationContext ctx) {
        Class<com.ctp.cdi.query.Query> query = com.ctp.cdi.query.Query.class;
        if (ctx.getMethod().isAnnotationPresent(query) &&
                ctx.getMethod().getAnnotation(query).lock() != LockModeType.NONE) {
            return ctx.getMethod().getAnnotation(query).lock();
        }
        return null;
    }
    
    protected boolean hasLockMode(QueryInvocationContext ctx) {
        return extractLockMode(ctx) != null;
    }
    
    protected Query applyRestrictions(QueryInvocationContext ctx, Query query) {
        Parameters params = ctx.getParams();
        if (params.hasSizeRestriction()) {
            query.setMaxResults(params.getSizeRestriciton());
        }
        if (params.hasFirstResult()) {
            query.setFirstResult(params.getFirstResult());
        }
        if (hasLockMode(ctx)) {
            query.setLockMode(extractLockMode(ctx));
        }
        return query;
    }
    
}
