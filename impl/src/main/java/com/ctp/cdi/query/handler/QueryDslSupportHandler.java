package com.ctp.cdi.query.handler;

import javax.persistence.EntityManager;

import com.ctp.cdi.query.criteria.QueryDslSupport;
import com.ctp.cdi.query.spi.DelegateQueryHandler;
import com.mysema.query.jpa.impl.JPAQuery;

public class QueryDslSupportHandler implements QueryDslSupport, DelegateQueryHandler {

    private final EntityManager entityManager;

    public static QueryDslSupportHandler newInstance(EntityManager entityManager) {
        return new QueryDslSupportHandler(entityManager);
    }

    private QueryDslSupportHandler(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public JPAQuery jpaQuery() {
        return new JPAQuery(entityManager);
    }

}
