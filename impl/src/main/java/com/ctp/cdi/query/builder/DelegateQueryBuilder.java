package com.ctp.cdi.query.builder;

import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import com.ctp.cdi.query.handler.EntityDaoHandler;
import com.ctp.cdi.query.handler.QueryInvocationContext;
import com.ctp.cdi.query.meta.MethodType;
import com.ctp.cdi.query.meta.QueryInvocation;

@QueryInvocation(MethodType.DELEGATE)
public class DelegateQueryBuilder extends QueryBuilder {

    @Override
    public Object execute(QueryInvocationContext queryContext) {
        try {
            InvocationContext context = queryContext.getInvocation();
            if (EntityDaoHandler.contains(context.getMethod())) {
                return callEntityHandler(context, queryContext.getEntityClass(), queryContext.getEntityManager());
            }
            return context.proceed();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delegate call", e);
        }
    }
    
    private Object callEntityHandler(InvocationContext ctx, Class<?> entityClass, EntityManager entityManager) throws Exception {
        return EntityDaoHandler.create(entityManager, entityClass).invoke(ctx.getMethod(), ctx.getParameters());
    }

}
