package com.ctp.cdi.query.handler;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;

import com.ctp.cdi.query.util.DaoUtils;
import java.lang.reflect.Modifier;

/**
 * Entry point for query processing.
 * 
 * @author thomashug
 */
public class QueryHandler {
    
    private static final Logger log = Logger.getLogger(QueryHandler.class);
    
    @Inject
    private Instance<EntityManager> entityManager;
    
    @AroundInvoke
    public Object handle(InvocationContext ctx) throws Exception {
        if (!Modifier.isAbstract(ctx.getMethod().getModifiers())) {
            return ctx.proceed();
        }
        if (BaseHandler.contains(ctx.getMethod())) {
            return callBaseHandler(ctx);
        }
	return null;
    }

    private Object callBaseHandler(InvocationContext ctx) throws Exception {
        Class<?> entityClass = DaoUtils.extractEntityMetaData(ctx.getTarget().getClass()).getEntityClass();
        return BaseHandler.create(entityManager.get(), entityClass).invoke(ctx.getMethod(), ctx.getParameters());
    }

}
