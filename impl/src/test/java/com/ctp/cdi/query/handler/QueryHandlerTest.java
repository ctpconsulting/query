package com.ctp.cdi.query.handler;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.domain.Simple2;
import com.ctp.cdi.query.test.service.Simple2Dao;
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.util.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

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

    @Test
    public void shouldDelegateToImplementation() {
        // given
        final String name = "testDelegateToImplementation";
        createSimple(name);

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
        createSimple(name);

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
        Simple simple = createSimple(name);

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
        createSimple(name);

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
        createSimple(name);

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
        createSimple(name);
        createSimple(name);

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
        createSimple(name);
        Simple second = createSimple(name);

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
        createSimple(name);

        // when
        Long result = dao.findCountByQuery(name);

        // then
        assertNotNull(result);
    }

    @Test
    public void shouldFindWithNativeQuery() {
        // given
        final String name = "testFindWithNativeQuery";
        createSimple(name);
        createSimple(name);

        // when
        List<Simple> result = dao.findWithNative(name);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    private Simple createSimple(String name) {
        Simple result = new Simple(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

    private Simple2 createSimple2(String name) {
        Simple2 result = new Simple2(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
