package com.ctp.cdi.query.spi;

import static org.junit.Assert.assertNotNull;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.MyEntityDao;
import com.ctp.cdi.query.test.service.MyEntityDaoDelegate;
import com.ctp.cdi.query.test.service.MySimpleDao;
import com.ctp.cdi.query.test.util.TestDeployments;

public class CdiQuerySpiTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
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
    public void should_call_delegate() {
        // given
        Simple simple = new Simple("test_call_delegate");

        // when
        simple = dao.saveAndFlushAndRefresh(simple);

        // then
        assertNotNull(simple.getId());
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
