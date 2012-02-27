package com.ctp.cdi.query.meta.unit;

import java.util.List;

class PersistenceUnit {
    
    public static final String RESOURCE_PATH = "META-INF/persistence.xml";
    public static final String DEFAULT_ORM_PATH = "META-INF/orm.xml";

    private final String unitName;
    private final List<EntityDescriptor> entities;

    PersistenceUnit(String unitName, List<EntityDescriptor> entities) {
        this.unitName = unitName;
        this.entities = entities;
    }
    
    public EntityDescriptor find(Class<?> entityClass) {
        for (EntityDescriptor entity : entities) {
            if (entity.is(entityClass)) {
                return entity;
            }
        }
        return null;
    }

    public String getUnitName() {
        return unitName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PersistenceUnit [unitName=").append(unitName)
               .append(", entities=").append(entities).append("]");
        return builder.toString();
    }

}
