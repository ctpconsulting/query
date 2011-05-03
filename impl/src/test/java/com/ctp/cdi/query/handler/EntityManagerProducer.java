package com.ctp.cdi.query.handler;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ctp.cdi.query.test.service.Simplistic;

public class EntityManagerProducer {

    @Produces @Simplistic
    @PersistenceContext
    private EntityManager entityManager;

}
