package org.apache.deltaspike.query.impl.handler;

import java.io.Serializable;
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

import org.apache.deltaspike.query.api.EntityManagerDao;
import org.apache.deltaspike.query.spi.DelegateQueryHandler;


@Typed(DelegateQueryHandler.class)
public class EntityManagerDelegateQueryHandler<E, PK extends Serializable> extends AbstractDelegateQueryHandler<E>
        implements EntityManagerDao<E, PK> {

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
    @SuppressWarnings("unchecked")
    public TypedQuery<E> createNamedQuery(String query) {
        return (TypedQuery<E>) delegate().createNamedQuery(query, context.getEntityClass());
    }

    @Override
    public Query createNativeQuery(String sql) {
        return delegate().createNamedQuery(sql);
    }

    @Override
    public Query createNativeQuery(String sql, String resultSetMapping) {
        return delegate().createNativeQuery(sql, resultSetMapping);
    }

    @Override
    @SuppressWarnings("unchecked")
    public TypedQuery<E> createQuery(String query) {
        return (TypedQuery<E>) delegate().createQuery(query, context.getEntityClass());
    }

    @Override
    public TypedQuery<E> createQuery(CriteriaQuery<E> query) {
        return delegate().createQuery(query);
    }

    @Override
    public void detach(Object entity) {
        delegate().detach(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E find(PK primaryKey) {
        return (E) delegate().find(context.getEntityClass(), primaryKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E find(PK primaryKey, Map<String, Object> properties) {
        return (E) delegate().find(context.getEntityClass(), primaryKey, properties);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E find(PK primaryKey, LockModeType lockMode) {
        return (E) delegate().find(context.getEntityClass(), primaryKey, lockMode);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E find(PK pk, LockModeType lockMode, Map<String, Object> properties) {
        return (E) delegate().find(context.getEntityClass(), pk, lockMode, properties);
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
    @SuppressWarnings("unchecked")
    public E getReference(PK pk) {
        return (E) delegate().getReference(context.getEntityClass(), pk);
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
    public void lock(E entity, LockModeType lockMode, Map<String, Object> properties) {
        delegate().lock(entity, lockMode, properties);
    }

    @Override
    public E merge(E entity) {
        return delegate().merge(entity);
    }

    @Override
    public void persist(E entity) {
        delegate().persist(entity);
    }

    @Override
    public void refresh(Object entity) {
        delegate().refresh(entity);
    }

    @Override
    public void refresh(E entity, Map<String, Object> properties) {
        delegate().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        delegate().refresh(entity, lockMode);
    }

    @Override
    public void refresh(E entity, LockModeType lockMode, Map<String, Object> properties) {
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
