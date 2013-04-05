package org.apache.deltaspike.data.test.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.deltaspike.data.api.EntityManagerResolver;

public class SimplisticEntityManagerResolver implements EntityManagerResolver
{

    @Inject @Simplistic
    private EntityManager entityManager;

    @Override
    public EntityManager resolveEntityManager()
    {
        return entityManager;
    }
}
