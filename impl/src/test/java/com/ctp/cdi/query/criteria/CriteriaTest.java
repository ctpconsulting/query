package com.ctp.cdi.query.criteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.OneToMany;
import com.ctp.cdi.query.test.domain.OneToOne;
import com.ctp.cdi.query.test.domain.Parent;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.ParentDao;
import com.ctp.cdi.query.test.service.SimpleCriteriaDao;
import com.ctp.cdi.query.test.service.Statistics;
import com.ctp.cdi.query.test.util.TestDeployments;

/**
 *
 * @author thomashug
 */
public class CriteriaTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
                .addClasses(SimpleCriteriaDao.class, ParentDao.class, Statistics.class)
                .addPackage(Simple.class.getPackage());
    }

    @Inject
    private SimpleCriteriaDao dao;

    @Inject
    private ParentDao parentDao;

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void should_create_criteria_query() {
        // given
        final String name = "testCreateCriteriaQuery";
        createSimple(name, 55);

        // when
        List<Simple> result1 = dao.queryByCriteria(name, Boolean.TRUE, 0, 50);
        List<Simple> result2 = dao.queryByCriteria(name, Boolean.TRUE, 50, 100);
        List<Simple> result3 = dao.queryByCriteria(name, Boolean.FALSE, 50, 100);

        // then
        assertEquals(0, result1.size());
        assertEquals(1, result2.size());
        assertEquals(0, result3.size());
    }

    @Test
    public void should_create_join_criteria_query() {
        // given
        final String name = "testCreateJoinCriteriaQuery";
        final String nameOne = name + "-one";
        final String nameMany = name + "-many";
        Parent parent = new Parent(name);
        parent.setOne(new OneToOne(nameOne));
        parent.add(new OneToMany(nameMany));

        entityManager.persist(parent);
        entityManager.flush();

        // when
        List<Parent> result = parentDao.joinQuery(name, nameOne, nameMany);

        // then
        assertEquals(1, result.size());
        assertNotNull(result.get(0));

        Parent queried = result.get(0);
        assertEquals(name, queried.getName());
        assertNotNull(queried.getOne());
        assertEquals(nameOne, queried.getOne().getName());
        assertEquals(1, queried.getMany().size());
        assertEquals(nameMany, queried.getMany().get(0).getName());
    }

    @Test
    public void should_create_or_query() {
        // given
        final String name = "testCreateOrQuery";
        Parent parent1 = new Parent(name + "1");
        parent1.setValue(25L);
        Parent parent2 = new Parent(name + "2");
        parent2.setValue(75L);
        Parent parent3 = new Parent(name + "3");
        parent3.setValue(25L);
        Parent parent4 = new Parent(name + "1");
        parent4.setValue(75L);

        entityManager.persist(parent1);
        entityManager.persist(parent2);
        entityManager.persist(parent3);
        entityManager.persist(parent4);
        entityManager.flush();

        // when
        List<Parent> result = parentDao.orQuery(name + "1", name + "2");

        // then
        assertEquals(2, result.size());
    }

    @Test
    public void should_create_ordered_query() {
        // given
        final String name = "testCreateOrderedQuery";
        Parent parent1 = new Parent(name + "99");
        Parent parent2 = new Parent(name + "12");
        Parent parent3 = new Parent(name + "19");
        Parent parent4 = new Parent(name + "02");

        entityManager.persist(parent1);
        entityManager.persist(parent2);
        entityManager.persist(parent3);
        entityManager.persist(parent4);
        entityManager.flush();

        // when
        List<Parent> result = parentDao.orderedQuery();

        // then
        assertEquals(4, result.size());
        assertEquals(name + "02", result.get(0).getName());
        assertEquals(name + "12", result.get(1).getName());
        assertEquals(name + "19", result.get(2).getName());
        assertEquals(name + "99", result.get(3).getName());
    }

    @Test
    public void should_create_query_wihtout_nulls() {
        // given
        final String name = "testCreateQueryWihtoutNulls";
        Parent parent = new Parent(name);

        entityManager.persist(parent);
        entityManager.flush();

        // when
        List<Parent> result = parentDao.nullAwareQuery(name, null, null);

        // then
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
    }

    @Test
    public void should_create_fetch_query() {
        // given
        final String name = "testCreateFetchQuery";
        Parent parent = new Parent(name);
        parent.add(new OneToMany(name + "-1"));
        parent.add(new OneToMany(name + "-2"));

        entityManager.persist(parent);
        entityManager.flush();

        // when
        Parent result = parentDao.fetchQuery(name);

        // then
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertNotNull(result.getMany());
        assertEquals(2, result.getMany().size());
    }

    @Test
    public void should_create_in_query() {
        // given
        final String name = "testCreateInQuery";
        Parent parent1 = new Parent(name + "-1");
        Parent parent2 = new Parent(name + "-2");
        Parent parent3 = new Parent(name + "-3");

        entityManager.persist(parent1);
        entityManager.persist(parent2);
        entityManager.persist(parent3);
        entityManager.flush();

        // when
        List<Parent> result = parentDao.fetchByName(name + "-1", name + "-2", name + "-3");

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void should_create_select_criteria_with_result_type() {
        // given
        final String name = "testCreateSelectCriteriaWithResultType";
        createSimple(name, 1);
        createSimple(name, 2);
        createSimple(name, 3);
        createSimple(name, 4);
        createSimple(name, 99);

        // when
        Statistics result = dao.queryWithSelect(name);

        // then
        assertNotNull(result.getAverage());
        assertEquals(Long.valueOf(5l), result.getCount());
    }

    @Test
    public void should_create_select_criteria_without_result_type() {
        // given
        final String name = "testCreateSelectCriteriaWithoutResultType";
        createSimple(name, 10);
        createSimple(name, 99);

        // when
        Object[] result = dao.queryWithSelectAggregateReturnArray(name);

        // then
        assertEquals(Integer.valueOf(10), result[0]);
        assertEquals(Integer.valueOf(99), result[1]);
        assertTrue(result[2] instanceof java.sql.Date);
        assertTrue(result[3] instanceof java.sql.Time);
        assertTrue(result[4] instanceof java.sql.Timestamp);
    }

    @Test
    public void should_create_select_criteria_with_attributes() {
        // given
        final String name = "testCreateSelectCriteriaWithAttributes";
        createSimple(name, 10);
        createSimple(name, 99);

        // when
        List<Object[]> results = dao.queryWithSelectAttributes(name);

        // then
        for (Object[] result : results) {
            assertEquals(name, result[0]);
            assertEquals(name.toUpperCase(), result[1]);
            assertEquals(name.toLowerCase(), result[2]);
            assertEquals(name.substring(1), result[3]);
            assertEquals(name.substring(1, 1+2), result[4]);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    private Simple createSimple(String name, Integer counter) {
        Simple result = new Simple(name);
        result.setCounter(counter);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
