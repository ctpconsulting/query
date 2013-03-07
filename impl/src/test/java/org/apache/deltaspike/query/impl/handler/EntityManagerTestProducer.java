package org.apache.deltaspike.query.impl.handler;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.deltaspike.query.test.service.Simplistic;


public class EntityManagerTestProducer {

    @Produces @Simplistic
    @PersistenceContext
    private EntityManager entityManager;

}
