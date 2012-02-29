package com.ctp.cdi.query;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.domain.SimpleBuilder;
import com.ctp.cdi.query.test.domain.Simple_;
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.util.Deployments;

public class QueryResultTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deployment() {
        return Deployments.initDeployment()
                .addClasses(SimpleDao.class)
                .addPackage(Simple.class.getPackage());
    }

    @Inject
    private SimpleDao dao;

    @Produces
    @PersistenceContext
    private EntityManager entityManager;
    
    private SimpleBuilder builder;

    @Test
    public void shouldSortResult() {
        // given
        final String name = "testSortResult";
        builder.createSimple(name, Integer.valueOf(99));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(56));
        builder.createSimple(name, Integer.valueOf(123));
        
        // when
        List<Simple> result = dao.findByName(name)
                .orderDesc(Simple_.counter)
                .getResultList();
        
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        int last = Integer.MAX_VALUE;
        for (Simple simple : result) {
            int current = simple.getCounter().intValue();
            assertTrue(current < last);
            last = current;
        }
    }
    
    @Before
    public void setup() {
        builder = new SimpleBuilder(entityManager);
    }
}
