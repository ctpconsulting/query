package org.apache.deltaspike.query.test.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.deltaspike.query.api.AbstractEntityDao;
import org.apache.deltaspike.query.test.domain.Simple;


public abstract class SimpleDaoWithOverriddenEntityManager extends AbstractEntityDao<Simple, Long> {
    
    @Override @Simplistic
    protected abstract EntityManager entityManager();

    public abstract List<Simple> findByName(String name);
    
    public List<Simple> findWithEm(String name) {
        return entityManager().createQuery("select s from Simple s where s.name = ?1", Simple.class)
                .setParameter(1, name)
                .getResultList();
    }

}
