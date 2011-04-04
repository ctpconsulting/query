package com.ctp.cdi.query.builder;

import static com.ctp.cdi.query.util.QueryUtils.isNotEmpty;

import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;

import com.ctp.cdi.query.Query;
import com.ctp.cdi.query.param.Parameters;

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
        if (ctx.getMethod().isAnnotationPresent(Query.class)) {
            Query query = ctx.getMethod().getAnnotation(Query.class);
            return isNotEmpty(query.value()) || isNotEmpty(query.named());
        }
        return false;
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
        javax.persistence.Query result = null;
        if (!"".equals(query.named())) {
            result = params.applyTo(entityManager.createNamedQuery(query.named()));
        } else {
            result = params.applyTo(entityManager.createQuery(query.value()));
        }
        return applyRestrictions(result);
    }

}
