/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctp.cdi.query.builder;

import javax.interceptor.InvocationContext;
import javax.persistence.Query;

import com.ctp.cdi.query.builder.part.QueryRoot;
import com.ctp.cdi.query.handler.QueryInvocationContext;
import com.ctp.cdi.query.meta.MethodType;
import com.ctp.cdi.query.meta.QueryInvocation;
import com.ctp.cdi.query.param.Parameters;
import com.ctp.cdi.query.util.EntityUtils;

/**
 *
 * @author thomashug
 */
@QueryInvocation(MethodType.PARSE)
public class MethodQueryBuilder extends QueryBuilder {
    
    public static boolean handles(InvocationContext ctx) {
        return ctx.getMethod().getName().startsWith(QueryRoot.QUERY_PREFIX);
    }
    
    @Override
    public Object execute(QueryInvocationContext ctx) {
        Query jpaQuery = createJpaQuery(ctx);
        if (returnsList(ctx)) {
            return jpaQuery.getResultList();
        } else {
            return jpaQuery.getSingleResult();
        }
        
    }
    
    private Query createJpaQuery(QueryInvocationContext ctx) {
        Parameters params = ctx.getParams();
        QueryRoot root = QueryRoot.create(ctx.getMethod().getName(), 
                EntityUtils.entityName(ctx.getEntityClass()), params);
        Query result = params.applyTo(ctx.getEntityManager().createQuery(root.createJpql()));
        return applyRestrictions(ctx, result);
    }
    
}
