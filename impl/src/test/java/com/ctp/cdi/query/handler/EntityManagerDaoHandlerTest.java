package com.ctp.cdi.query.handler;

import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple3;
import com.ctp.cdi.query.test.service.SimpleEntityManagerDao;
import com.ctp.cdi.query.test.util.TestDeployments;

public class EntityManagerDaoHandlerTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
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
    public void should_persist_new_entity() {
        // given
        Simple3 simple = new Simple3();
        
        // when
        dao.persist(simple);
        
        // then
        assertTrue(simple.getId() > 0);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
