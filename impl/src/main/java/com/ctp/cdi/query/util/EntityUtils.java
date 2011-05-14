package com.ctp.cdi.query.util;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jboss.seam.solder.properties.Property;
import org.jboss.seam.solder.properties.query.AnnotatedPropertyCriteria;
import org.jboss.seam.solder.properties.query.PropertyQueries;
import org.jboss.seam.solder.properties.query.PropertyQuery;

import com.ctp.cdi.query.meta.NonEntityException;

public final class EntityUtils {

    private EntityUtils() {
    }
    
    public static boolean isNew(Object entity) {
        assertIsEntity(entity);
        Property<Serializable> property = primaryKey(entity.getClass());
        property.setAccessible();
        Serializable value = property.getValue(entity);
        return value == null;
    }
    
    public static Class<? extends Serializable> primaryKeyClass(Class<?> entityClass) {
        Property<Serializable> property = primaryKey(entityClass);
        return property.getJavaClass();
    }
    
    public static String entityName(Class<?> entityClass) {
        Entity entity = entityClass.getAnnotation(Entity.class);
        return !"".equals(entity.name()) ? entity.name() : entityClass.getSimpleName();
    }
    
    public static boolean isEntityClass(Class<?> entityClass) {
        return entityClass.isAnnotationPresent(Entity.class);
    }
    
    private static Property<Serializable> primaryKey(Class<?> entityClass) {
        PropertyQuery<Serializable> query = PropertyQueries.<Serializable>createQuery(entityClass)
                                                           .addCriteria(new AnnotatedPropertyCriteria(Id.class));
        return query.getFirstResult();
    }
    
    private static void assertIsEntity(Object entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Provided object is null");
        }
        if (!isEntityClass(entity.getClass())) {
            throw new NonEntityException("Provided object is not an @Entity");
        }
    }
}
