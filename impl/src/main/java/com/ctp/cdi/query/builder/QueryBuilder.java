package com.ctp.cdi.query.builder;

import com.ctp.cdi.query.param.Parameters;
import com.ctp.cdi.query.util.DaoUtils;
import java.util.List;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import org.jboss.logging.Logger;

/**
 * Query builder factory. Delegates to concrete implementations.
 * 
 * @author thomashug
 */
public abstract class QueryBuilder {
    
    private static final Logger log = Logger.getLogger(QueryBuilder.class);
    
    protected final Parameters params;
    protected final InvocationContext ctx;
    protected final Class<?> entityClass;
    
    protected QueryBuilder(Parameters params, InvocationContext ctx) {
        this.params = params;
        this.ctx = ctx;
        this.entityClass = DaoUtils.extractEntityMetaData(
                ctx.getTarget().getClass()).getClass();
    }
    
    public static QueryBuilder create(InvocationContext ctx) {
        Parameters params = Parameters.create(ctx.getMethod(), ctx.getParameters());
        if (AnnotatedQueryBuilder.handles(ctx)) {
            log.debug("create: Using annotation based query builder.");
            return new AnnotatedQueryBuilder(params, ctx);
        }
        log.warn("create: No query builder found.");
        return null;
    }
    
    public abstract Object execute(EntityManager entityManager);
    
    protected boolean returnsList() {
        return ctx.getMethod().getReturnType().isAssignableFrom(List.class);
    }
    
}
