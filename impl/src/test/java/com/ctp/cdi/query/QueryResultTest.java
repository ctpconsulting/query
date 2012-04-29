package com.ctp.cdi.query;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
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
import com.ctp.cdi.query.test.util.TestDeployments;

public class QueryResultTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
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
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(56));
        builder.createSimple(name, Integer.valueOf(123));
        
        // when
        List<Simple> result = dao.findByName(name)
                .orderDesc(Simple_.counter)
                .orderAsc(Simple_.id)
                .getResultList();
        
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        int lastCounter = Integer.MAX_VALUE;
        long lastId = Long.MIN_VALUE;
        for (Simple simple : result) {
            int currentCounter = simple.getCounter().intValue();
            long currentId = simple.getId().longValue();
            if (currentCounter == lastCounter) {
                assertTrue(currentId > lastId);
            } else {
                assertTrue(currentCounter < lastCounter);
            }
            lastCounter = currentCounter;
            lastId = currentId;
        }
    }
    
    @Test
    public void shouldPageResult() {
        // given
        final String name = "testPageResult";
        builder.createSimple(name, Integer.valueOf(99));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(56));
        builder.createSimple(name, Integer.valueOf(123));
        
        // when
        List<Simple> result = dao.findByName(name)
                .hint("javax.persistence.query.timeout", 10000)
                .lockMode(LockModeType.NONE)
                .flushMode(FlushModeType.COMMIT)
                .orderDesc(Simple_.counter)
                .orderAsc(Simple_.id)
                .firstResult(2)
                .maxResults(2)
                .getResultList();
        
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(56, result.get(0).getCounter().intValue());
    }
    
    @Test
    public void shouldModifyNamedQuery() {
        // given
        final String name = "testModifyNamedQuery";
        builder.createSimple(name + 0);
        builder.createSimple(name + 1);
        builder.createSimple(name + 2);
        builder.createSimple(name + 3);
        
        // when
        List<Simple> result = dao.queryResultWithNamed(name + "%")
                 .orderDesc(Simple_.name)
                 .getResultList();
        
        // then
        assertEquals(4, result.size());
        assertEquals(name + 3, result.get(0).getName());
        assertEquals(name + 2, result.get(1).getName());
    }
    
    @Before
    public void setup() {
        builder = new SimpleBuilder(entityManager);
    }
}
