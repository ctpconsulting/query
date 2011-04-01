package com.ctp.cdi.query.handler;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.util.EntityUtils;

/**
 * Implement basic functionality from the {@link EntityDao}.
 * @author thomashug
 *
 * @param <E>
 * @param <PK>
 */
public class BaseHandler<E, PK extends Serializable> implements EntityDao<E, PK> {
    
    public static final String QUERY_ALL = "from {0}";
    public static final String QUERY_COUNT = "select count(e) from {0} e";
    
    private final EntityManager entityManager;
    private final Class<E> entityClass;
    
    public BaseHandler(EntityManager entityManager, Class<E> entityClass) {
	if (null == entityManager) {
            throw new IllegalStateException("EntityManager cannot be null.");
        }
	this.entityManager = entityManager;
	this.entityClass = entityClass;
    }
    
    public static boolean contains(Method method) {
        return extract(method) != null;
    }
    
    public static <E, PK extends Serializable> BaseHandler<E, PK> create(EntityManager e, Class<E> entityClass) {
        return new BaseHandler<E, PK>(e, entityClass);
    }
    
    public Object invoke(Method method, Object[] args) throws InvocationTargetException, IllegalArgumentException, IllegalAccessException {
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
        // TODO implement
        return Collections.emptyList();
    }

    @Override
    public List<E> findAll() {
	return entityManager.createQuery(allQuery(), entityClass)
	        .getResultList();
    }

    @Override
    public Long count() {
        return entityManager.createQuery(countQuery(), Long.class)
            .getSingleResult();
    }

    @Override
    public void remove(E entity) {
	entityManager.remove(entity);
    }

    @Override
    public void flush() {
	entityManager.flush();
    }
    
    public static Method extract(Method method) {
        try {
            String name = method.getName();
            return BaseHandler.class.getMethod(name, method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    private String allQuery() {
        return MessageFormat.format(QUERY_ALL, EntityUtils.entityName(entityClass));
    }
    
    private String countQuery() {
        return MessageFormat.format(QUERY_COUNT, EntityUtils.entityName(entityClass));
    }

}
