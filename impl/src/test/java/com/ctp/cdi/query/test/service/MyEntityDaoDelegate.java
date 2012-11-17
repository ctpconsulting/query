package com.ctp.cdi.query.test.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.ctp.cdi.query.spi.DelegateQueryHandler;
import com.ctp.cdi.query.spi.QueryInvocationContext;

public class MyEntityDaoDelegate<E> implements DelegateQueryHandler, MyEntityDao<E> {

    @Inject
    private QueryInvocationContext context;

    @Override
    public E saveAndFlushAndRefresh(E entity) {
        entityManager().persist(entity);
        entityManager().flush();
        entityManager().refresh(entity);
        return entity;
    }
    
    private EntityManager entityManager() {
        return context.getEntityManager();
    }

}
