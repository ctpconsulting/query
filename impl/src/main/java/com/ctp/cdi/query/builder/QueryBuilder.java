package com.ctp.cdi.query.builder;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;

import javax.persistence.*;

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
    
    public abstract Object execute(QueryInvocationContext ctx) throws Exception;
    
    protected boolean returnsList(Method method) {
        return method.getReturnType().isAssignableFrom(List.class);
    }
    
    protected LockModeType extractLockMode(Method method) {
        Class<com.ctp.cdi.query.Query> query = com.ctp.cdi.query.Query.class;
        if (method.isAnnotationPresent(query) &&
                method.getAnnotation(query).lock() != LockModeType.NONE) {
            return method.getAnnotation(query).lock();
        }
        return null;
    }
    
    protected boolean hasLockMode(Method method) {
        return extractLockMode(method) != null;
    }

    protected QueryHint[] extractQueryHints(Method method) {
        Class<com.ctp.cdi.query.Query> query = com.ctp.cdi.query.Query.class;
        if (method.isAnnotationPresent(query) &&
                method.getAnnotation(query).hints().length > 0) {
            return method.getAnnotation(query).hints();
        }
        return null;
    }

    protected boolean hasQueryHints(Method method) {
        return extractQueryHints(method) != null;
    }

    protected Query applyRestrictions(QueryInvocationContext context, Query query) {
        Parameters params = context.getParams();
        Method method = context.getMethod();
        if (params.hasSizeRestriction()) {
            query.setMaxResults(params.getSizeRestriciton());
        }
        if (params.hasFirstResult()) {
            query.setFirstResult(params.getFirstResult());
        }
        if (hasLockMode(method)) {
            query.setLockMode(extractLockMode(method));
        }
        if (hasQueryHints(method)) {
            QueryHint[] hints = extractQueryHints(method);
            for (QueryHint hint : hints) {
                query.setHint(hint.name(), hint.value());
            }
        }
        query = context.applyJpaQueryPostProcessors(query);
        return query;
    }
    
}
