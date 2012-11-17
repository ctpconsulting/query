package com.ctp.cdi.query.handler;

import java.util.Map;

import javax.enterprise.inject.Typed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import com.ctp.cdi.query.spi.DelegateQueryHandler;

@Typed(DelegateQueryHandler.class)
public class EntityManagerDelegateQueryHandler<E> extends AbstractDelegateQueryHandler<E>
        implements EntityManager {
    
    @Override
    public void clear() {
        delegate().clear();
    }

    @Override
    public void close() {
        delegate().close();
    }

    @Override
    public boolean contains(Object entity) {
        return delegate().contains(entity);
    }

    @Override
    public Query createNamedQuery(String query) {
        return delegate().createNamedQuery(query);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String query, Class<T> clazz) {
        return delegate().createNamedQuery(query, clazz);
    }

    @Override
    public Query createNativeQuery(String sql) {
        return delegate().createNamedQuery(sql);
    }

    @Override
    public Query createNativeQuery(String sql, Class resultClass) {
        return delegate().createNativeQuery(sql, resultClass);
    }

    @Override
    public Query createNativeQuery(String sql, String resultSetMapping) {
        return delegate().createNativeQuery(sql, resultSetMapping);
    }

    @Override
    public Query createQuery(String query) {
        return delegate().createQuery(query);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> query) {
        return delegate().createQuery(query);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String query, Class<T> resultClass) {
        return delegate().createQuery(query, resultClass);
    }

    @Override
    public void detach(Object entity) {
        delegate().detach(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object pk) {
        return delegate().find(entityClass, pk);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return delegate().find(entityClass, primaryKey, properties);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object pk, LockModeType lockMode) {
        return delegate().find(entityClass, pk, lockMode);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object pk, LockModeType lockMode, Map<String, Object> properties) {
        return delegate().find(entityClass, pk, lockMode, properties);
    }

    @Override
    public void flush() {
        delegate().flush();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate().getCriteriaBuilder();
    }

    @Override
    public Object getDelegate() {
        return delegate().getDelegate();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return delegate().getEntityManagerFactory();
    }

    @Override
    public FlushModeType getFlushMode() {
        return delegate().getFlushMode();
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return delegate().getLockMode(entity);
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate().getMetamodel();
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate().getProperties();
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object pk) {
        return delegate().getReference(entityClass, pk);
    }

    @Override
    public EntityTransaction getTransaction() {
        return delegate().getTransaction();
    }

    @Override
    public boolean isOpen() {
        return delegate().isOpen();
    }

    @Override
    public void joinTransaction() {
        delegate().joinTransaction();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        delegate().lock(entity, lockMode);
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        delegate().lock(entity, lockMode, properties);
    }

    @Override
    public <T> T merge(T entity) {
        return delegate().merge(entity);
    }

    @Override
    public void persist(Object entity) {
        delegate().persist(entity);
    }

    @Override
    public void refresh(Object entity) {
        delegate().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        delegate().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        delegate().refresh(entity, lockMode);        
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        delegate().refresh(entity, lockMode, properties);
    }

    @Override
    public void remove(Object entity) {
        delegate().remove(entity);
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        delegate().setFlushMode(flushMode);
    }

    @Override
    public void setProperty(String name, Object value) {
        delegate().setProperty(name, value);
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        return delegate().unwrap(clazz);
    }
    
    private EntityManager delegate() {
        return context.getEntityManager();
    }

}
