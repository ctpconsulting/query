package com.ctp.cdi.query.meta.unit;

import java.io.Serializable;

import com.ctp.cdi.query.property.query.NamedPropertyCriteria;
import com.ctp.cdi.query.property.query.PropertyQueries;
import com.ctp.cdi.query.property.query.PropertyQuery;

abstract class PersistentClassDescriptor {

    final String name;
    final Class<?> entityClass;
    final Class<? extends Serializable> idClass;
    final String id;
    private PersistentClassDescriptor parent;

    PersistentClassDescriptor(String name, String packageName, String className, String idClass, String id) {
        Class<?> clazz = entityClass(className, packageName);
        this.name = name;
        this.entityClass = clazz;
        this.idClass = idClass(clazz, idClass, packageName, id);
        this.id = id;
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

    public Class<?> getEntityClass() {
        return entityClass;
    }

    String className(Class<?> clazz) {
        return clazz == null ? null : clazz.getSimpleName();
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
        if (entity == null || id == null) {
            return null;
        }
        PropertyQuery<Serializable> query = PropertyQueries.<Serializable>createQuery(entity)
                .addCriteria(new NamedPropertyCriteria(id));
        return query.getFirstResult().getJavaClass();
    }

    private String buildClassName(String clazzName, String packageName) {
        if (clazzName == null && packageName == null) {
            return null;
        }
        return (packageName != null && !isClassNameQualified(clazzName)) ? packageName + "." + clazzName : clazzName;
    }

    private boolean isClassNameQualified(String name) {
        return name.contains(".");
    }

    public PersistentClassDescriptor getParent() {
        return parent;
    }

    public void setParent(PersistentClassDescriptor parent) {
        this.parent = parent;
    }

}
