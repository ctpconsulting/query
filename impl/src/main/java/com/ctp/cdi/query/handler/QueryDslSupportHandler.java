package com.ctp.cdi.query.handler;

import org.jboss.solder.core.Requires;

import com.ctp.cdi.query.criteria.QueryDslSupport;
import com.mysema.query.jpa.impl.JPAQuery;

@Requires("com.mysema.query.jpa.impl.JPAQuery")
public class QueryDslSupportHandler<E> extends AbstractDelegateQueryHandler<E>
        implements QueryDslSupport {

    @Override
    public JPAQuery jpaQuery() {
        return new JPAQuery(getEntityManager());
    }

}
