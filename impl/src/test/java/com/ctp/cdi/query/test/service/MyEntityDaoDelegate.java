package com.ctp.cdi.query.test.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.ctp.cdi.query.spi.DelegateQueryHandler;
import com.ctp.cdi.query.spi.QueryInvocationContext;
import com.ctp.cdi.query.test.domain.Simple;

public class MyEntityDaoDelegate implements DelegateQueryHandler, MyEntityDao<Simple> {

    @Inject
    private QueryInvocationContext context;

    @Override
    public Simple saveAndFlushAndRefresh(Simple entity) {
        entityManager().persist(entity);
        entityManager().flush();
        entityManager().refresh(entity);
        return entity;
    }
    
    private EntityManager entityManager() {
        return context.getEntityManager();
    }


}
