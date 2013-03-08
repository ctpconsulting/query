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


public interface EntityManagerDao<E, PK extends Serializable>
{

    void clear();

    void close();

    boolean contains(E entity) ;

    TypedQuery<E> createNamedQuery(String query);

    Query createNativeQuery(String sql);

    Query createNativeQuery(String sql, String resultSetMapping);

    TypedQuery<E> createQuery(CriteriaQuery<E> query);

    TypedQuery<E> createQuery(String query);

    void detach(E entity);

    E find(PK primaryKey);

    E find(PK primaryKey, Map<String, Object> properties);

    E find(PK pk, LockModeType lockMode);

    E find(PK pk, LockModeType lockMode, Map<String, Object> properties);

    void flush();

    CriteriaBuilder getCriteriaBuilder();

    Object getDelegate();

    EntityManagerFactory getEntityManagerFactory();

    FlushModeType getFlushMode();

    LockModeType getLockMode(E entity);

    Metamodel getMetamodel();

    Map<String, Object> getProperties();

    E getReference(PK pk);

    EntityTransaction getTransaction();

    boolean isOpen();

    void joinTransaction();

    void lock(Object entity, LockModeType lockMode);

    void lock(E entity, LockModeType lockMode, Map<String, Object> properties);

    E merge(E entity);

    void persist(E entity);

    void refresh(E entity);

    void refresh(E entity, Map<String, Object> properties);

    void refresh(E entity, LockModeType lockMode);

    void refresh(E entity, LockModeType lockMode, Map<String, Object> properties);

    void remove(E entity);

    void setFlushMode(FlushModeType flushMode);

    void setProperty(String name, Object value);

    <T> T unwrap(Class<T> clazz);

}
