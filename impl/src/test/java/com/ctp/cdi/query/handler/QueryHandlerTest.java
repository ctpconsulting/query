package com.ctp.cdi.query.handler;

import static junit.framework.Assert.assertEquals;
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
import com.ctp.cdi.query.test.domain.Simple2;
import com.ctp.cdi.query.test.domain.SimpleBuilder;
import com.ctp.cdi.query.test.service.Simple2Dao;
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.util.Deployments;

public class QueryHandlerTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deployment() {
        return Deployments.initDeployment()
                .addClasses(SimpleDao.class, Simple2Dao.class)
                .addPackage(Simple.class.getPackage());
    }

    @Inject
    private SimpleDao dao;

    @Inject
    private Simple2Dao dao2;

    @Produces
    @PersistenceContext
    private EntityManager entityManager;
    
    private SimpleBuilder builder;

    @Test
    public void shouldDelegateToImplementation() {
        // given
        final String name = "testDelegateToImplementation";
        builder.createSimple(name);

        // when
        List<Simple> result = dao.implementedQueryByName(name);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void shouldCreateNamedQueryIndex() {
        // given
        final String name = "testCreateNamedQueryIndex";
        builder.createSimple(name);

        // when
        List<Simple> result = dao.findByNamedQueryIndexed(name, Boolean.TRUE);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
    }

    @Test
    public void shouldCreateNamedQueryNamed() {
        // given
        final String name = "testCreateNamedQueryNamed";
        Simple simple = builder.createSimple(name);

        // when
        Simple result = dao.findByNamedQueryNamed(simple.getId(), Boolean.TRUE);

        // then
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    public void shouldRunAnnotatedQuery() {
        // given
        final String name = "testRunAnnotatedQuery";
        builder.createSimple(name);

        // when
        Simple result = dao.findByQuery(name);

        // then
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    public void shouldCreateQueryByMethodName() {
        // given
        final String name = "testCreateQueryByMethodName";
        builder.createSimple(name);

        // when
        Simple result = dao.findByNameAndEnabled(name, Boolean.TRUE);

        // then
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    public void shouldRestrictResultSizeByAnnotation() {
        // given
        final String name = "testRestrictResultSizeByAnnotation";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        List<Simple> result = dao.findByNamedQueryIndexed(name, Boolean.TRUE);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void shouldRestrictResultSizeByParameters() {
        // given
        final String name = "testRestrictResultSizeByParameters";
        builder.createSimple(name);
        Simple second = builder.createSimple(name);

        // when
        List<Simple> result = dao.findByNamedQueryRestricted(name, Boolean.TRUE, 1, 1);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(second.getId(), result.get(0).getId());
    }

    @Test
    public void shouldWorkWith2ndDao() {
        // given
        final String name = "testWorkWith2ndDao";
        Simple2 simple = createSimple2(name);

        // when
        Simple2 result = dao2.findByName(name);

        // then
        assertNotNull(result);
        assertEquals(simple.getId(), result.getId());
        assertEquals(name, result.getName());
    }

    @Test
    public void shouldReturnAggregate() {
        // given
        final String name = "testReturnAggregate";
        builder.createSimple(name);

        // when
        Long result = dao.findCountByQuery(name);

        // then
        assertNotNull(result);
    }

    @Test
    public void shouldFindWithNativeQuery() {
        // given
        final String name = "testFindWithNativeQuery";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        List<Simple> result = dao.findWithNative(name);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    
    @Test
    public void shouldOrderResultByMethodOrderBy() {
        // given
        final String name = "testFindWithNativeQuery";
        builder.createSimple(name);
        builder.createSimple(name);
        builder.createSimple(name);
        
        // when
        List<Simple> result = dao.findByOrderByIdDesc();
        
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        long last = Long.MAX_VALUE;
        for (Simple simple : result) {
            assertTrue(simple.getId().longValue() < last);
            last = simple.getId().longValue();
        }
    }
    
    @Test
    public void shouldExecuteUpdate() {
        // given
        final String name = "testFindWithNativeQuery";
        final String newName = "testFindWithNativeQueryUpdated" + System.currentTimeMillis();
        Simple s = builder.createSimple(name);

        // when
        int count = dao.updateNameForId(newName, s.getId());

        // then
        assertEquals(1, count);
    }
    
    @Before
    public void setup() {
        builder = new SimpleBuilder(entityManager);
    }

    private Simple2 createSimple2(String name) {
        Simple2 result = new Simple2(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
