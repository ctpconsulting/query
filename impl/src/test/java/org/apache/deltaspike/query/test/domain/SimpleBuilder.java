package org.apache.deltaspike.query.test.domain;

import javax.persistence.EntityManager;

public class SimpleBuilder {

    private final EntityManager entityManager;

    public SimpleBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public Simple createSimple(String name, Integer counter) {
        Simple result = new Simple(name);
        result.setCounter(counter);
        return persistSimple(result);
    }
    
    public Simple createSimple(String name) {
        Simple result = new Simple(name);
        return persistSimple(result);
    }
    
    public Simple persistSimple(Simple result) {
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
