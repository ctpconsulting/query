package com.ctp.cdi.query.handler;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.ctp.cdi.query.spi.DelegateQueryHandler;

public class QueryDslSupportProducer {

    boolean enabled;

    @Inject
    private CdiQueryInvocationContext context;

    @PostConstruct
    public void init() {
        try {
            Class.forName("com.mysema.query.jpa.impl.JPAQuery");
            enabled = true;
        } catch (ClassNotFoundException e) {
            enabled = false;
        }
    }

    @Produces
    public DelegateQueryHandler queryDsl() {
        if (enabled) {
            return QueryDslSupportHandler.newInstance(context.getEntityManager());
        }
        return null;
    }

}
