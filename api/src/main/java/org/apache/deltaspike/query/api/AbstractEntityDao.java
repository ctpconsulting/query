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

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;


/**
 * Base DAO class to be extended by concrete implementations.
 * @author thomashug
 *
 * @param <E>   Entity type.
 * @param <PK>  Primary key type.
 */
@Dao
public abstract class AbstractEntityDao<E, PK extends Serializable>
        implements EntityDao<E, PK>
{

    /**
     * Utility method to get hold of the entity manager for this DAO.
     * This method can be overridden and decorated with qualifiers. If done, the qualifiers
     * will be used to resolve a specific entity manager other than the default one.
     *
     * @return          Entity manager instance.
     */
    protected abstract EntityManager entityManager();

    /**
     * Utility method to create a criteria query.
     * @return          Criteria query
     */
    protected abstract CriteriaQuery<E> criteriaQuery();

    /**
     * Get the entity class this DAO is related to.
     * @return          DAO entity class.
     */
    protected abstract Class<E> entityClass();

}
