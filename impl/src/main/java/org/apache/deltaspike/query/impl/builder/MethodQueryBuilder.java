package org.apache.deltaspike.query.impl.builder;

import javax.persistence.Query;

import org.apache.deltaspike.query.impl.builder.part.QueryRoot;
import org.apache.deltaspike.query.impl.handler.CdiQueryInvocationContext;
import org.apache.deltaspike.query.impl.meta.MethodType;
import org.apache.deltaspike.query.impl.meta.QueryInvocation;
import org.apache.deltaspike.query.impl.param.Parameters;


/**
 * 
 * @author thomashug
 */
@QueryInvocation(MethodType.PARSE)
public class MethodQueryBuilder extends QueryBuilder {
    
    @Override
    public Object execute(CdiQueryInvocationContext context) {
        Query jpaQuery = createJpaQuery(context);
        return context.executeQuery(jpaQuery);
    }
    
    private Query createJpaQuery(CdiQueryInvocationContext context) {
        Parameters params = context.getParams();
        QueryRoot root = context.getDaoMethod().getQueryRoot();
        String jpqlQuery = context.applyQueryStringPostProcessors(root.getJpqlQuery());
        context.setQueryString(jpqlQuery);
        Query result = params.applyTo(context.getEntityManager().createQuery(jpqlQuery));
        return applyRestrictions(context, result);
    }
    
}
