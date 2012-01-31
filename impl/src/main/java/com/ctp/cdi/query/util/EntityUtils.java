package com.ctp.cdi.query.util;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.ctp.cdi.query.meta.NonEntityException;
import org.jboss.solder.properties.Property;
import org.jboss.solder.properties.query.AnnotatedPropertyCriteria;
import org.jboss.solder.properties.query.PropertyQueries;
import org.jboss.solder.properties.query.PropertyQuery;

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

    @SuppressWarnings("unchecked")
    public static Class<? extends Serializable> primaryKeyClass(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(IdClass.class)) {
            return entityClass.getAnnotation(IdClass.class).value(); // Serializablity isn't required, could cause problems
        }
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

        if (query.getFirstResult() == null) {
            query = PropertyQueries.<Serializable>createQuery(entityClass)
                    .addCriteria(new AnnotatedPropertyCriteria(EmbeddedId.class));
        }

        if (query.getFirstResult() == null) {
            throw new IllegalStateException("Class " + entityClass + " has no id defined");
        }
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
