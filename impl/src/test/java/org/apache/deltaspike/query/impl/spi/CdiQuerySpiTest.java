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
package org.apache.deltaspike.query.impl.spi;

import static org.junit.Assert.assertNotNull;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.deltaspike.query.test.TransactionalTestCase;
import org.apache.deltaspike.query.test.domain.Simple;
import org.apache.deltaspike.query.test.service.MyEntityDao;
import org.apache.deltaspike.query.test.service.MyEntityDaoDelegate;
import org.apache.deltaspike.query.test.service.MySimpleDao;
import org.apache.deltaspike.query.test.util.TestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

public class CdiQuerySpiTest extends TransactionalTestCase
{

    @Deployment
    public static Archive<?> deployment()
    {
        return TestDeployments.initDeployment()
                .addClasses(MySimpleDao.class,
                        MyEntityDao.class,
                        MyEntityDaoDelegate.class)
                .addPackage(Simple.class.getPackage());
    }

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private MySimpleDao dao;

    @Test
    public void should_call_delegate()
    {
        // given
        Simple simple = new Simple("test_call_delegate");

        // when
        simple = dao.saveAndFlushAndRefresh(simple);

        // then
        assertNotNull(simple.getId());
    }

    @Override
    protected EntityManager getEntityManager()
    {
        return entityManager;
    }
}
