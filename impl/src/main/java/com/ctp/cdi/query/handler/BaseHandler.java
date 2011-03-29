package com.ctp.cdi.query.handler;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;

import javax.persistence.EntityManager;

import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.util.EntityUtils;

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
    
    public boolean contains(Method method) {
	return false;
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

    @Override
    public boolean exists(PK primaryKey) {
	return findBy(primaryKey) != null;
    }
    
    private String allQuery() {
        return MessageFormat.format(QUERY_ALL, EntityUtils.entityName(entityClass));
    }
    
    private String countQuery() {
        return MessageFormat.format(QUERY_COUNT, EntityUtils.entityName(entityClass));
    }

}
