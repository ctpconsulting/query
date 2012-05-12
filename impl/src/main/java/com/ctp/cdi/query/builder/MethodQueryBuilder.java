package com.ctp.cdi.query.builder;

import javax.persistence.Query;

import com.ctp.cdi.query.builder.part.QueryRoot;
import com.ctp.cdi.query.handler.QueryInvocationContext;
import com.ctp.cdi.query.meta.MethodType;
import com.ctp.cdi.query.meta.QueryInvocation;
import com.ctp.cdi.query.param.Parameters;

/**
 * 
 * @author thomashug
 */
@QueryInvocation(MethodType.PARSE)
public class MethodQueryBuilder extends QueryBuilder {
    
    @Override
    public Object execute(QueryInvocationContext context) {
        Query jpaQuery = createJpaQuery(context);
        return context.executeQuery(jpaQuery);
    }
    
    private Query createJpaQuery(QueryInvocationContext context) {
        Parameters params = context.getParams();
        QueryRoot root = context.getDaoMethod().getQueryRoot();
        String jpqlQuery = context.applyQueryStringPostProcessors(root.getJpqlQuery());
        context.setQueryString(jpqlQuery);
        Query result = params.applyTo(context.getEntityManager().createQuery(jpqlQuery));
        return applyRestrictions(context, result);
    }
    
}
