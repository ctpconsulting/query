package org.apache.deltaspike.query.impl.meta.verifier;

import javax.persistence.Entity;

import org.apache.deltaspike.query.impl.meta.unit.PersistenceUnits;


public class EntityVerifier implements Verifier<Class<?>> {

    @Override
    public boolean verify(Class<?> entity) {
        return entity.isAnnotationPresent(Entity.class) || PersistenceUnits.instance().isEntity(entity);
    }

}
