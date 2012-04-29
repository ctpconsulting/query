package com.ctp.cdi.query.builder;

import static com.ctp.cdi.query.util.QueryUtils.isNotEmpty;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import com.ctp.cdi.query.Query;
import com.ctp.cdi.query.handler.QueryInvocationContext;
import com.ctp.cdi.query.meta.MethodType;
import com.ctp.cdi.query.meta.QueryInvocation;
import com.ctp.cdi.query.param.Parameters;
import com.ctp.cdi.query.util.jpa.QueryStringExtractorFactory;

/**
 * Create the query based on method annotations.
 * @author thomashug
 */
@QueryInvocation(MethodType.ANNOTATED)
public class AnnotatedQueryBuilder extends QueryBuilder {
    
    private final QueryStringExtractorFactory factory = new QueryStringExtractorFactory();

    @Override
    public Object execute(QueryInvocationContext context) {
        Method method = context.getMethod();
        Query query = method.getAnnotation(Query.class);
        javax.persistence.Query jpaQuery = createJpaQuery(query, context);
        return context.executeQuery(jpaQuery);
    }
    
    private javax.persistence.Query createJpaQuery(Query query, QueryInvocationContext context) {
        EntityManager entityManager = context.getEntityManager();
        Parameters params = context.getParams();
        javax.persistence.Query result = null;
        if (isNotEmpty(query.named())) {
            if (!context.hasQueryStringPostProcessors()) {
                result = params.applyTo(entityManager.createNamedQuery(query.named()));
            } else {
                javax.persistence.Query namedQuery = entityManager.createNamedQuery(query.named());
                String named = factory.select(namedQuery).extractFrom(namedQuery);
                String jpqlQuery = context.applyQueryStringPostProcessors(named);
                result = params.applyTo(entityManager.createQuery(jpqlQuery));
            }
        } else if (isNotEmpty(query.sql())) {
            String jpqlQuery = context.applyQueryStringPostProcessors(query.sql());
            result = params.applyTo(entityManager.createNativeQuery(jpqlQuery));
        } else {
            String jpqlQuery = context.applyQueryStringPostProcessors(query.value());
            result = params.applyTo(entityManager.createQuery(jpqlQuery));
        }
        return applyRestrictions(context, result);
    }

}
