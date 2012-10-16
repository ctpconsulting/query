package com.ctp.cdi.query.handler;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.ctp.cdi.query.spi.DelegateQueryHandler;

public abstract class AbstractDelegateQueryHandler<E> implements DelegateQueryHandler {

    @Inject
    protected CdiQueryInvocationContext context;

    protected Class<E> getEntityClass() {
        return (Class<E>) context.getEntityClass();
    }

    protected EntityManager getEntityManager() {
        return context.getEntityManager();
    }

}
