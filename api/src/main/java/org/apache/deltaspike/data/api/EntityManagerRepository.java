/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.deltaspike.data.api;

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

/**
 * Mirrors a {@link javax.persistence.EntityManager}.
 * @author thomashug
 *
 * @param <E>   Entity type
 * @param <PK>  Primary key type
 */
public interface EntityManagerRepository<E, PK extends Serializable>
{

    /**
     * See {@link javax.persistence.EntityManager#clear()}.
     */
    void clear();

    /**
     * See {@link javax.persistence.EntityManager#close()}.
     */
    void close();

    /**
     * See {@link javax.persistence.EntityManager#contains(Object)}.
     */
    boolean contains(Object entity) ;

    /**
     * See {@link javax.persistence.EntityManager#createNamedQuery(String, Class)}.
     */
    TypedQuery<E> createNamedQuery(String query);

    /**
     * See {@link javax.persistence.EntityManager#createNativeQuery(String, Class)}.
     */
    Query createNativeQuery(String sql);

    /**
     * See {@link javax.persistence.EntityManager#createNativeQuery(String, String)}.
     */
    Query createNativeQuery(String sql, String resultSetMapping);

    /**
     * See {@link javax.persistence.EntityManager#createQuery(String, Class)}.
     */
    TypedQuery<E> createQuery(CriteriaQuery<E> query);

    /**
     * See {@link javax.persistence.EntityManager#createQuery(String, Class)}.
     */
    TypedQuery<E> createQuery(String query);

    /**
     * See {@link javax.persistence.EntityManager#detach(Object)}.
     */
    void detach(Object entity);

    /**
     * See {@link javax.persistence.EntityManager#find(Class, Object)}.
     */
    E find(PK primaryKey);

    /**
     * See {@link javax.persistence.EntityManager#find(Class, Object, Map)}.
     */
    E find(PK primaryKey, Map<String, Object> properties);

    /**
     * See {@link javax.persistence.EntityManager#find(Class, Object, LockModeType)}.
     */
    E find(PK pk, LockModeType lockMode);

    /**
     * See {@link javax.persistence.EntityManager#find(Class, Object, LockModeType, Map)}.
     */
    E find(PK pk, LockModeType lockMode, Map<String, Object> properties);

    /**
     * See {@link javax.persistence.EntityManager#flush()}.
     */
    void flush();

    /**
     * See {@link javax.persistence.EntityManager#getCriteriaBuilder()}.
     */
    CriteriaBuilder getCriteriaBuilder();

    /**
     * See {@link javax.persistence.EntityManager#getDelegate()}.
     */
    Object getDelegate();

    /**
     * See {@link javax.persistence.EntityManager#getEntityManagerFactory()}.
     */
    EntityManagerFactory getEntityManagerFactory();

    /**
     * See {@link javax.persistence.EntityManager#getFlushMode()}.
     */
    FlushModeType getFlushMode();

    /**
     * See {@link javax.persistence.EntityManager#getLockMode(Object)}.
     */
    LockModeType getLockMode(Object entity);

    /**
     * See {@link javax.persistence.EntityManager#getMetamodel()}.
     */
    Metamodel getMetamodel();

    /**
     * See {@link javax.persistence.EntityManager#getProperties()}.
     */
    Map<String, Object> getProperties();

    /**
     * See {@link javax.persistence.EntityManager#getReference(Class, Object)}.
     */
    E getReference(PK pk);

    /**
     * See {@link javax.persistence.EntityManager#getTransaction()}.
     */
    EntityTransaction getTransaction();

    /**
     * See {@link javax.persistence.EntityManager#isOpen()}.
     */
    boolean isOpen();

    /**
     * See {@link javax.persistence.EntityManager#joinTransaction()}.
     */
    void joinTransaction();

    /**
     * See {@link javax.persistence.EntityManager#lock(Object, LockModeType)}.
     */
    void lock(Object entity, LockModeType lockMode);

    /**
     * See {@link javax.persistence.EntityManager#lock(Object, LockModeType, Map)}.
     */
    void lock(Object entity, LockModeType lockMode, Map<String, Object> properties);

    /**
     * See {@link javax.persistence.EntityManager#merge(Object)}.
     */
    E merge(E entity);

    /**
     * See {@link javax.persistence.EntityManager#persist(Object)}.
     */
    void persist(E entity);

    /**
     * See {@link javax.persistence.EntityManager#refresh(Object)}.
     */
    void refresh(E entity);

    /**
     * See {@link javax.persistence.EntityManager#refresh(Object, Map)}.
     */
    void refresh(E entity, Map<String, Object> properties);

    /**
     * See {@link javax.persistence.EntityManager#refresh(Object, LockModeType)}.
     */
    void refresh(E entity, LockModeType lockMode);

    /**
     * See {@link javax.persistence.EntityManager#refresh(Object, LockModeType, Map)}.
     */
    void refresh(E entity, LockModeType lockMode, Map<String, Object> properties);

    /**
     * See {@link javax.persistence.EntityManager#remove(Object)}.
     */
    void remove(E entity);

    /**
     * See {@link javax.persistence.EntityManager#setFlushMode(FlushModeType)}.
     */
    void setFlushMode(FlushModeType flushMode);

    /**
     * See {@link javax.persistence.EntityManager#setProperty(String, Object)}.
     */
    void setProperty(String name, Object value);

    /**
     * See {@link javax.persistence.EntityManager#unwrap(Class)}.
     */
    <T> T unwrap(Class<T> clazz);

}
