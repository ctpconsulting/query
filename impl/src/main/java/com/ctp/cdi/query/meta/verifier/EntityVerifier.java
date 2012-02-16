package com.ctp.cdi.query.meta.verifier;

import javax.persistence.Entity;

import com.ctp.cdi.query.meta.unit.PersistenceUnits;

public class EntityVerifier implements Verifier<Class<?>> {

    @Override
    public boolean verify(Class<?> entity) {
        return entity.isAnnotationPresent(Entity.class) || PersistenceUnits.instance().isEntity(entity);
    }

}
