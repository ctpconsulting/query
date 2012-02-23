package com.ctp.cdi.query.meta.unit;

import java.io.Serializable;

import org.jboss.solder.properties.query.NamedPropertyCriteria;
import org.jboss.solder.properties.query.PropertyQueries;
import org.jboss.solder.properties.query.PropertyQuery;

class EntityMapping {

    private final String name;
    private final Class<?> entityClass;
    private final Class<? extends Serializable> idClass;
    private final String id;

    EntityMapping(String name, String packageName, String className, String idClass, String id) {
        Class<?> entityClass = entityClass(className, packageName);
        this.name = name;
        this.entityClass = entityClass;
        this.idClass = idClass(entityClass, idClass, packageName, id);
        this.id = id;
    }
    
    public boolean is(Class<?> entityClass) {
        return this.entityClass.equals(entityClass);
    }

    public Class<? extends Serializable> getIdClass() {
        return idClass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityMapping [name=").append(name)
               .append(", entityClass=").append(entityClass)
               .append(", idClass=").append(idClass)
               .append(", id=").append(id).append("]");
        return builder.toString();
    }
    
    private Class<?> entityClass(String entityClass, String packageName) {
        try {
            String clazzName = buildClassName(entityClass, packageName);
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can't create class " + buildClassName(entityClass, packageName), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private Class<? extends Serializable> idClass(Class<?> entity, String idClass, String packageName, String id) {
        try {
            return (Class<? extends Serializable>) 
                    (idClass != null ? Class.forName(buildClassName(idClass, packageName)) : lookupIdClass(entity, id));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Failed to get ID class", e);
        }
    }
    
    private Class<?> lookupIdClass(Class<?> entity, String id) {
        PropertyQuery<Serializable> query = PropertyQueries.<Serializable>createQuery(entity)
                .addCriteria(new NamedPropertyCriteria(id));
        return query.getFirstResult().getJavaClass();
    }
    
    private String buildClassName(String clazzName, String packageName) {
        return (packageName != null && !isClassNameQualified(clazzName)) ? packageName + "." + clazzName : clazzName;
    }
    
    private boolean isClassNameQualified(String name) {
        return name.contains(".");
    }
    
}
