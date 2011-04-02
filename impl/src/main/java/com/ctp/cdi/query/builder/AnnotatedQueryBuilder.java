package com.ctp.cdi.query.builder;

import com.ctp.cdi.query.Query;
import com.ctp.cdi.query.param.Parameters;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import org.jboss.logging.Logger;

/**
 * Create the query based on method annotations.
 * @author thomashug
 */
public class AnnotatedQueryBuilder extends QueryBuilder {
    
    private static final Logger log = Logger.getLogger(AnnotatedQueryBuilder.class);

    public AnnotatedQueryBuilder(Parameters params, InvocationContext ctx) {
        super(params, ctx);
    }
    
    public static boolean handles(InvocationContext ctx) {
        return ctx.getMethod().isAnnotationPresent(Query.class);
    }

    @Override
    public Object execute(EntityManager entityManager) {
        Query query = ctx.getMethod().getAnnotation(Query.class);
        javax.persistence.Query jpaQuery = createJpaQuery(query, entityManager);
        if (returnsList()) {
            return jpaQuery.getResultList();
        } else {
            return jpaQuery.getSingleResult();
        }
    }
    
    private javax.persistence.Query createJpaQuery(Query query, EntityManager entityManager) {
        if (!"".equals(query.named())) {
            return params.applyTo(entityManager.createNamedQuery(query.named()));
        } else {
            return params.applyTo(entityManager.createQuery(query.value()));
        }
    }

}
