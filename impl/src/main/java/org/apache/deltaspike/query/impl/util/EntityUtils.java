package org.apache.deltaspike.query.impl.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.apache.deltaspike.query.impl.meta.unit.PersistenceUnits;
import org.apache.deltaspike.query.impl.meta.verifier.EntityVerifier;
import org.apache.deltaspike.query.impl.property.Property;
import org.apache.deltaspike.query.impl.property.query.AnnotatedPropertyCriteria;
import org.apache.deltaspike.query.impl.property.query.NamedPropertyCriteria;
import org.apache.deltaspike.query.impl.property.query.PropertyCriteria;
import org.apache.deltaspike.query.impl.property.query.PropertyQueries;
import org.apache.deltaspike.query.impl.property.query.PropertyQuery;


public final class EntityUtils {

    private EntityUtils() {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Class<? extends Serializable> primaryKeyClass(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(IdClass.class)) {
            return entityClass.getAnnotation(IdClass.class).value(); // Serializablity isn't required, could cause problems
        }
        Class clazz = PersistenceUnits.instance().primaryKeyIdClass(entityClass);
        if (clazz != null) {
            return clazz;
        }
        Property<Serializable> property = primaryKey(entityClass);
        return property.getJavaClass();
    }

    public static Object primaryKeyValue(Object entity) {
        Property<Serializable> property = primaryKey(entity.getClass());
        return property.getValue(entity);
    }

    public static String entityName(Class<?> entityClass) {
        String result = null;
        if (entityClass.isAnnotationPresent(Entity.class)) {
            result = entityClass.getAnnotation(Entity.class).name();
        } else {
            result = PersistenceUnits.instance().entityName(entityClass);
        }
        return (result != null && !"".equals(result)) ? result : entityClass.getSimpleName();
    }

    public static boolean isEntityClass(Class<?> entityClass) {
        return new EntityVerifier().verify(entityClass);
    }

    private static Property<Serializable> primaryKey(Class<?> entityClass) {
        for (PropertyCriteria c : criteriaList(entityClass)) {
            PropertyQuery<Serializable> query = PropertyQueries.<Serializable>createQuery(entityClass)
                    .addCriteria(c);
            if (query.getFirstResult() != null) {
                return query.getFirstResult();
            }
        }
        throw new IllegalStateException("Class " + entityClass + " has no id defined");
    }

    private static List<PropertyCriteria> criteriaList(Class<?> entityClass) {
        List<PropertyCriteria> criteria = new LinkedList<PropertyCriteria>();
        criteria.add(new AnnotatedPropertyCriteria(Id.class));
        criteria.add(new AnnotatedPropertyCriteria(EmbeddedId.class));
        String fromMappingFiles = PersistenceUnits.instance().primaryKeyField(entityClass);
        if (fromMappingFiles != null) {
            criteria.add(new NamedPropertyCriteria(fromMappingFiles));
        }
        return criteria;
    }
}
