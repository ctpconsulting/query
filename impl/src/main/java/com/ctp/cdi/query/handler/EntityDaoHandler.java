package com.ctp.cdi.query.handler;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;

import org.jboss.solder.logging.Logger;
import org.jboss.solder.properties.Property;
import org.jboss.solder.properties.query.NamedPropertyCriteria;
import org.jboss.solder.properties.query.PropertyQueries;

import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.util.EntityUtils;

/**
 * Implement basic functionality from the {@link EntityDao}.
 *
 * @author thomashug
 *
 * @param <E>   Entity type.
 * @param <PK>  Primary key type, must be a serializable.
 */
public class EntityDaoHandler<E, PK extends Serializable> implements EntityDao<E, PK> {

    private final Logger log = Logger.getLogger(EntityDaoHandler.class);

    private final EntityManager entityManager;
    private final Class<E> entityClass;
    private final String entityName;

    public EntityDaoHandler(EntityManager entityManager, Class<E> entityClass) {
        if (null == entityManager) {
            throw new IllegalStateException("EntityManager cannot be null.");
        }
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.entityName = EntityUtils.entityName(entityClass);
    }

    public static boolean contains(Method method) {
        return extract(method) != null;
    }

    public static <E, PK extends Serializable> EntityDaoHandler<E, PK> create(EntityManager e, Class<E> entityClass) {
        return new EntityDaoHandler<E, PK>(e, entityClass);
    }

    public Object invoke(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return extract(method).invoke(this, args);
    }

    @Override
    public E save(E entity) {
        if (EntityUtils.isNew(entity)) {
            entityManager.persist(entity);
            return entity;
        }
        return entityManager.merge(entity);
    }

    @Override
    public E saveAndFlush(E entity) {
        E result = save(entity);
        entityManager.flush();
        return result;
    }

    @Override
    public void refresh(E entity) {
        entityManager.refresh(entity);
    }

    @Override
    public E findBy(PK primaryKey) {
        return entityManager.find(entityClass, primaryKey);
    }

    @Override
    public List<E> findBy(E example, SingularAttribute<E, ?>... attributes) {
        return findBy(example,-1,-1,attributes);
    }

    public List<E> findBy(E example, int start, int max, SingularAttribute<E, ?>... attributes) {
        //Not sure if this should be the intended behaviour
        //when we don't get any attributes maybe we should
        //return a empty list instead of all results
        if(attributes == null || attributes.length == 0) {
            return findAll(start,max);
        }

        List<Property<Object>> properties = extractProperties(attributes);
        String jpqlQuery = exampleQuery(allQuery(),properties);
        log.debugv("findBy: Created query {0}", jpqlQuery);
        TypedQuery<E> query = entityManager.createQuery(jpqlQuery, entityClass);

        //set starting position
        if(start > 0) {
            query.setFirstResult(start);
        }

        //set maximum results
        if(max > 0) {
            query.setMaxResults(max);
        }

        addParameters(query, example, properties);
        return query.getResultList();
    }

    @Override
    public List<E> findAll() {
        return entityManager.createQuery(allQuery(), entityClass).getResultList();
    }

    @Override
    public List<E> findAll(int start, int max) {
        TypedQuery<E> query = entityManager.createQuery(allQuery(),entityClass);
        if(start > 0) {
            query.setFirstResult(start);
        }

        if(max > 0) {
            query.setMaxResults(max);
        }
        return query.getResultList();
    }

    @Override
    public Long count() {
        return entityManager.createQuery(countQuery(), Long.class).getSingleResult();
    }

    @Override
    public Long count(E example, SingularAttribute<E, ?>... attributes) {

        if(attributes == null || attributes.length == 0) {
            return count();
        }

        List<Property<Object>> properties = extractProperties(attributes);
        String jpqlQuery = exampleQuery(countQuery(),properties);
        log.debugv("count: Created query {0}", jpqlQuery);
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        addParameters(query, example, properties);
        return query.getSingleResult();
    }

    @Override
    public void remove(E entity) {
        entityManager.remove(entity);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public CriteriaQuery<E> criteriaQuery() {
        return entityManager.getCriteriaBuilder().createQuery(entityClass);
    }

    public Class<E> entityClass() {
        return entityClass;
    }

    private static Method extract(Method method) {
        try {
            String name = method.getName();
            return EntityDaoHandler.class.getMethod(name, method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String allQuery() {
        return QueryBuilder.selectQuery(entityName);
    }

    private String countQuery() {
        return QueryBuilder.countQuery(entityName);
    }

    private String exampleQuery(String queryBase, List<Property<Object>> properties) {
        StringBuilder jpqlQuery = new StringBuilder(queryBase).append(" where ");
        jpqlQuery.append(prepareWhere(properties));
        return jpqlQuery.toString();
    }

    private void addParameters(TypedQuery<?> query, E example, List<Property<Object>> properties) {
        for (Property<Object> property : properties) {
            property.setAccessible();
            query.setParameter(property.getName(), property.getValue(example));
        }
    }

    private String prepareWhere(List<Property<Object>> properties) {
        Iterator<Property<Object>> iterator = properties.iterator();
        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            String name = iterator.next().getName();
            result.append("e.").append(name).append(" = :").append(name).append(iterator.hasNext() ? " and " : "");

        }
        return result.toString();
    }

    private List<String> extractPropertyNames(SingularAttribute<E, ?>... attributes) {
        List<String> result = new ArrayList<String>(attributes.length);
        for (SingularAttribute<E, ?> attribute : attributes) {
            result.add(attribute.getName());
        }
        return result;
    }
    
    private  List<Property<Object>> extractProperties(SingularAttribute...attributes) {
        List<String> names = extractPropertyNames(attributes);
        List<Property<Object>> properties = PropertyQueries.createQuery(entityClass)
                .addCriteria(new NamedPropertyCriteria(names.toArray(new String[] {}))).getResultList();
        return properties;
    }

}
