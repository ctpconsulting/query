package org.apache.deltaspike.query.api;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;


public interface EntityManagerDao<E, PK extends Serializable> {

    void clear();

    void close();

    boolean contains(E entity) ;

    TypedQuery<E> createNamedQuery(String query);

    Query createNativeQuery(String sql);

    public Query createNativeQuery(String sql, String resultSetMapping);

    public TypedQuery<E> createQuery(CriteriaQuery<E> query);

    public TypedQuery<E> createQuery(String query);

    public void detach(E entity);

    public E find(PK primaryKey);

    public E find(PK primaryKey, Map<String, Object> properties);

    public E find(PK pk, LockModeType lockMode);

    public E find(PK pk, LockModeType lockMode, Map<String, Object> properties);

    public void flush();

    public CriteriaBuilder getCriteriaBuilder();

    public Object getDelegate();

    public EntityManagerFactory getEntityManagerFactory();

    public FlushModeType getFlushMode();

    public LockModeType getLockMode(E entity);

    public Metamodel getMetamodel();

    public Map<String, Object> getProperties();

    public E getReference(PK pk);

    public EntityTransaction getTransaction();

    public boolean isOpen();

    public void joinTransaction();

    public void lock(Object entity, LockModeType lockMode);

    public void lock(E entity, LockModeType lockMode, Map<String, Object> properties);

    public E merge(E entity);

    public void persist(E entity);

    public void refresh(E entity);

    public void refresh(E entity, Map<String, Object> properties);

    public void refresh(E entity, LockModeType lockMode);

    public void refresh(E entity, LockModeType lockMode, Map<String, Object> properties);

    public void remove(E entity);

    public void setFlushMode(FlushModeType flushMode);

    public void setProperty(String name, Object value);

    public <T> T unwrap(Class<T> clazz);

}
