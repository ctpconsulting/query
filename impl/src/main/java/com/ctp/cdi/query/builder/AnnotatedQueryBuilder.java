package com.ctp.cdi.query.builder;

import static com.ctp.cdi.query.util.QueryUtils.isNotEmpty;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import com.ctp.cdi.query.Query;
import com.ctp.cdi.query.handler.QueryInvocationContext;
import com.ctp.cdi.query.meta.MethodType;
import com.ctp.cdi.query.meta.QueryInvocation;
import com.ctp.cdi.query.param.Parameters;

/**
 * Create the query based on method annotations.
 * @author thomashug
 */
@QueryInvocation(MethodType.ANNOTATED)
public class AnnotatedQueryBuilder extends QueryBuilder {
    
    @Override
    public Object execute(QueryInvocationContext context) {
        Method method = context.getMethod();
        Query query = method.getAnnotation(Query.class);
        javax.persistence.Query jpaQuery = createJpaQuery(query, context);
        if (returnsList(method)) {
            return jpaQuery.getResultList();
        } else {
            return jpaQuery.getSingleResult();
        }
    }
    
    private javax.persistence.Query createJpaQuery(Query query, QueryInvocationContext context) {
        EntityManager entityManager = context.getEntityManager();
        Parameters params = context.getParams();
        javax.persistence.Query result = null;
        if (isNotEmpty(query.named())) {
            result = params.applyTo(entityManager.createNamedQuery(query.named()));
        } else if (isNotEmpty(query.sql())) {
            result = params.applyTo(entityManager.createNativeQuery(query.sql()));
        } else {
            result = params.applyTo(entityManager.createQuery(query.value()));
        }
        return applyRestrictions(context, result);
    }

}
