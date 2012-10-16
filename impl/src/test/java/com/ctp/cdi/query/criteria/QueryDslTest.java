package com.ctp.cdi.query.criteria;

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
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.service.SimpleQueryDslDao;
import com.ctp.cdi.query.test.util.TestDeployments;
import com.mysema.query.jpa.impl.JPAQuery;

public class QueryDslTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
                          .addPackage(SimpleDao.class.getPackage())
                          .addPackage(Simple.class.getPackage());
    }
    
    @Produces
    @PersistenceContext
    private EntityManager entityManager;
    
    @Inject
    private SimpleQueryDslDao dao;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    @Test
    public void should_instantiate_dsl_query() {
        // when
        JPAQuery query = dao.jpaQuery();
        
        // then
        assertNotNull(query);
    }
    
}
