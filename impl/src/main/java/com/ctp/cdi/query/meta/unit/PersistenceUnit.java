package com.ctp.cdi.query.meta.unit;

import java.util.List;

class PersistenceUnit {
    
    public static final String RESOURCE_PATH = "META-INF/persistence.xml";
    public static final String DEFAULT_ORM_PATH = "META-INF/orm.xml";

    private final String unitName;
    private final List<EntityMapping> entities;

    PersistenceUnit(String unitName, List<EntityMapping> entities) {
        this.unitName = unitName;
        this.entities = entities;
    }
    
    public EntityMapping find(Class<?> entityClass) {
        for (EntityMapping file : entities) {
            if (file.is(entityClass)) {
                return file;
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
               .append(", mappingFiles=").append(entities).append("]");
        return builder.toString();
    }

}
