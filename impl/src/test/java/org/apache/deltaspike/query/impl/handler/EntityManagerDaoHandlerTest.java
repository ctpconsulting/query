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
package org.apache.deltaspike.query.impl.handler;

import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.deltaspike.query.test.TransactionalTestCase;
import org.apache.deltaspike.query.test.domain.Simple3;
import org.apache.deltaspike.query.test.service.SimpleEntityManagerDao;
import org.apache.deltaspike.query.test.util.TestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

public class EntityManagerDaoHandlerTest extends TransactionalTestCase
{

    @Deployment
    public static Archive<?> deployment()
    {
        return TestDeployments.initDeployment()
                .addClasses(SimpleEntityManagerDao.class)
                .addPackage(Simple3.class.getPackage());
    }

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private SimpleEntityManagerDao dao;

    @Test
    public void should_persist_new_entity()
    {
        // given
        Simple3 simple = new Simple3();

        // when
        dao.persist(simple);

        // then
        assertTrue(simple.getId() > 0);
    }

    @Override
    protected EntityManager getEntityManager()
    {
        return entityManager;
    }

}
