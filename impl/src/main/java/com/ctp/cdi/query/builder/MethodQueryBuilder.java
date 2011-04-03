/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctp.cdi.query.builder;

import com.ctp.cdi.query.builder.part.QueryRoot;
import com.ctp.cdi.query.param.Parameters;
import com.ctp.cdi.query.util.DaoUtils;
import com.ctp.cdi.query.util.EntityUtils;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author thomashug
 */
public class MethodQueryBuilder extends QueryBuilder {

    public MethodQueryBuilder(Parameters params, InvocationContext ctx) {
        super(params, ctx);
    }
    
    public static boolean handles(InvocationContext ctx) {
        return ctx.getMethod().getName().startsWith(QueryRoot.QUERY_PREFIX);
    }
    
    @Override
    public Object execute(EntityManager entityManager) {
        Query jpaQuery = createJpaQuery(entityManager);
        if (returnsList()) {
            return jpaQuery.getResultList();
        } else {
            return jpaQuery.getSingleResult();
        }
        
    }
    
    private Query createJpaQuery(EntityManager entityManager) {
        QueryRoot root = QueryRoot.create(ctx.getMethod().getName(), 
                EntityUtils.entityName(entityClass));
        return params.applyTo(entityManager.createQuery(root.createJpql()));
    }
    
}
