package com.ctp.cdi.query.builder;

import java.text.MessageFormat;
import java.util.List;

import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.ctp.cdi.query.param.Parameters;
import com.ctp.cdi.query.util.DaoUtils;

/**
 * Query builder factory. Delegates to concrete implementations.
 * 
 * @author thomashug
 */
public abstract class QueryBuilder {
    
    public static final String QUERY_SELECT = "select e from {0} e";
    public static final String QUERY_COUNT = "select count(e) from {0} e";
    public static final String ENTITY_NAME = "e";
    
    private static final Logger log = Logger.getLogger(QueryBuilder.class);
    
    protected final Parameters params;
    protected final InvocationContext ctx;
    protected final Class<?> entityClass;
    
    protected QueryBuilder(Parameters params, InvocationContext ctx) {
        this.params = params;
        this.ctx = ctx;
        this.entityClass = DaoUtils.extractEntityMetaData(
                ctx.getTarget().getClass()).getEntityClass();
    }
    
    public static QueryBuilder create(InvocationContext ctx) {
        Parameters params = Parameters.create(ctx.getMethod(), ctx.getParameters());
        if (AnnotatedQueryBuilder.handles(ctx)) {
            log.debug("create: Using annotation based query builder.");
            return new AnnotatedQueryBuilder(params, ctx);
        }
        if (MethodQueryBuilder.handles(ctx)) {
            log.debug("create: Using method based query builder.");
            return new MethodQueryBuilder(params, ctx);
        }
        log.warn("create: No query builder found.");
        return null;
    }
    
    public static String selectQuery(String entityName) {
        return MessageFormat.format(QUERY_SELECT, entityName);
    }
    
    public static String countQuery(String entityName) {
        return MessageFormat.format(QUERY_COUNT, entityName);
    }
    
    public abstract Object execute(EntityManager entityManager);
    
    protected boolean returnsList() {
        return ctx.getMethod().getReturnType().isAssignableFrom(List.class);
    }
    
    protected LockModeType extractLockMode() {
        Class<com.ctp.cdi.query.Query> query = com.ctp.cdi.query.Query.class;
        if (ctx.getMethod().isAnnotationPresent(query) &&
                ctx.getMethod().getAnnotation(query).lock() != LockModeType.NONE) {
            return ctx.getMethod().getAnnotation(query).lock();
        }
        return null;
    }
    
    protected boolean hasLockMode() {
        return extractLockMode() != null;
    }
    
    protected Query applyRestrictions(Query query) {
        if (params.hasSizeRestriction()) {
            query.setMaxResults(params.getSizeRestriciton());
        }
        if (params.hasFirstResult()) {
            query.setFirstResult(params.getFirstResult());
        }
        if (hasLockMode()) {
            query.setLockMode(extractLockMode());
        }
        return query;
    }
    
}
