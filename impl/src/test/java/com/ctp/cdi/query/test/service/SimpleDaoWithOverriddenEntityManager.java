package com.ctp.cdi.query.test.service;

import java.util.List;

import javax.persistence.EntityManager;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.test.domain.Simple;

public abstract class SimpleDaoWithOverriddenEntityManager extends AbstractEntityDao<Simple, Long> {
    
    @Override @Simplistic
    protected abstract EntityManager getEntityManager();

    public abstract List<Simple> findByName(String name);
    
    public List<Simple> findWithEm(String name) {
        return getEntityManager().createQuery("select s from Simple s where s.name = ?1", Simple.class)
                .setParameter(1, name)
                .getResultList();
    }

}
