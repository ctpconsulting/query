package com.ctp.cdi.query.handler;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.ExtendedDaoInterface;
import com.ctp.cdi.query.test.util.Deployments;

public class BaseHandlerTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
        return Deployments.initDeployment()
                .addClasses(ExtendedDaoInterface.class)
                .addPackage(Simple.class.getPackage());
    }
    
    @Inject
    private ExtendedDaoInterface dao;
    
    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldCountAll() throws Exception {
        // given
        createSimple();
        
        // when
        Long result = dao.count();
        
        // then
        Assert.assertEquals(Long.valueOf(1), result);
    }
    
    private void createSimple() throws Exception {
        entityManager.persist(new Simple());
        entityManager.flush();
    }

}
