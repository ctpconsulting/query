package org.apache.deltaspike.query.impl.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.deltaspike.query.test.TransactionalTestCase;
import org.apache.deltaspike.query.test.domain.Simple;
import org.apache.deltaspike.query.test.domain.Simple2;
import org.apache.deltaspike.query.test.domain.SimpleBuilder;
import org.apache.deltaspike.query.test.service.Simple2Dao;
import org.apache.deltaspike.query.test.service.SimpleDao;
import org.apache.deltaspike.query.test.util.TestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;


public class QueryHandlerTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
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
    public void should_delegate_to_implementation() {
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
    public void should_create_named_query_index() {
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
    public void should_create_named_query_named() {
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
    public void should_run_annotated_query() {
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
    public void should_create_query_by_method_name() {
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
    public void should_restrict_result_size_by_annotation() {
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
    public void should_restrict_result_size_by_parameters() {
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
    public void should_work_with_2nd_dao() {
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
    public void should_return_aggregate() {
        // given
        final String name = "testReturnAggregate";
        builder.createSimple(name);

        // when
        Long result = dao.findCountByQuery(name);

        // then
        assertNotNull(result);
    }

    @Test
    public void should_find_with_native_query() {
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
    public void should_order_result_by_method_order_by() {
        // given
        final String name = "testFindWithNativeQuery";
        builder.createSimple(name, Integer.valueOf(33));
        builder.createSimple(name, Integer.valueOf(66));
        builder.createSimple(name, Integer.valueOf(66));
        builder.createSimple(name, Integer.valueOf(22));
        builder.createSimple(name, Integer.valueOf(55));
        
        // when
        List<Simple> result = dao.findByOrderByCounterAscIdDesc();
        
        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        long lastId = Long.MAX_VALUE;
        int lastCounter = Integer.MIN_VALUE;
        for (Simple simple : result) {
            long currentId = simple.getId().longValue();
            int currentCounter = simple.getCounter().intValue();
            if (currentCounter == lastCounter) {
                assertTrue(currentId < lastId);                
            } else {
                assertTrue(currentCounter > lastCounter);
            }
            lastId = currentId;
            lastCounter = currentCounter;
        }
    }
    
    @Test
    public void should_execute_update() {
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
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    private Simple2 createSimple2(String name) {
        Simple2 result = new Simple2(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
