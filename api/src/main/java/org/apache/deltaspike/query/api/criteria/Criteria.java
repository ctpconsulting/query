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
package org.apache.deltaspike.query.api.criteria;

import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Criteria API utilities.
 *
 * @author thomashug
 *
 * @param <C> Entity type.
 * @param <R> Result type.
 */
public interface Criteria<C, R>
{

    /**
     * Executes the query and returns the result list.
     *
     * @return List of entities matching the query.
     */
    List<R> getResultList();

    /**
     * Executes the query which has a single result.
     *
     * @return Entity matching the search query.
     */
    R getSingleResult();

    /**
     * Creates a JPA query object to be executed.
     *
     * @return A {@link TypedQuery} object ready to return results.
     */
    TypedQuery<R> createQuery();

    Criteria<C, R> or(Criteria<C, R>... criteria);

    Criteria<C, R> or(Collection<Criteria<C, R>> criteria);

    <P, E> Criteria<C, R> join(SingularAttribute<? super C, P> att, Criteria<P, P> criteria);

    <P, E> Criteria<C, R> join(ListAttribute<? super C, P> att, Criteria<P, P> criteria);

    <P, E> Criteria<C, R> join(CollectionAttribute<? super C, P> att, Criteria<P, P> criteria);

    <P, E> Criteria<C, R> join(SetAttribute<? super C, P> att, Criteria<P, P> criteria);

    <P, E> Criteria<C, R> join(MapAttribute<? super C, E, P> att, Criteria<P, P> criteria);

    <P, E> Criteria<C, R> fetch(SingularAttribute<? super C, P> att);

    <P, E> Criteria<C, R> fetch(SingularAttribute<? super C, P> att, JoinType joinType);

    <P, E> Criteria<C, R> fetch(PluralAttribute<? super C, P, E> att);

    <P, E> Criteria<C, R> fetch(PluralAttribute<? super C, P, E> att, JoinType joinType);

    <P> Criteria<C, R> orderAsc(SingularAttribute<? super C, P> att);

    <P> Criteria<C, R> orderDesc(SingularAttribute<? super C, P> att);

    <N> Criteria<C, N> select(Class<N> resultClass, QuerySelection<? super C, ?>... selection);

    Criteria<C, Object[]> select(QuerySelection<? super C, ?>... selection);

    Criteria<C, R> distinct();

    <P> Criteria<C, R> eq(SingularAttribute<? super C, P> att, P value);

    <P> Criteria<C, R> notEq(SingularAttribute<? super C, P> att, P value);

    <P> Criteria<C, R> like(SingularAttribute<? super C, String> att, String value);

    <P> Criteria<C, R> notLike(SingularAttribute<? super C, String> att, String value);

    <P extends Number> Criteria<C, R> lt(SingularAttribute<? super C, P> att, P value);

    <P extends Comparable<? super P>> Criteria<C, R> ltOrEq(SingularAttribute<? super C, P> att, P value);

    <P extends Number> Criteria<C, R> gt(SingularAttribute<? super C, P> att, P value);

    <P extends Comparable<? super P>> Criteria<C, R> gtOrEq(SingularAttribute<? super C, P> att, P value);

    <P extends Comparable<? super P>> Criteria<C, R> between(SingularAttribute<? super C, P> att, P lower, P upper);

    <P> Criteria<C, R> isNull(SingularAttribute<? super C, P> att);

    <P> Criteria<C, R> notNull(SingularAttribute<? super C, P> att);

    <P extends Collection<?>> Criteria<C, R> empty(SingularAttribute<? super C, P> att);

    <P extends Collection<?>> Criteria<C, R> notEmpty(SingularAttribute<? super C, P> att);

    <P> Criteria<C, R> in(SingularAttribute<? super C, P> att, P... values);

    List<Predicate> predicates(CriteriaBuilder builder, Path<C> path);

}
