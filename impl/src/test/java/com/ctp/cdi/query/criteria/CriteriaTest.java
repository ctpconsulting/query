package com.ctp.cdi.query.criteria;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.OneToMany;
import com.ctp.cdi.query.test.domain.OneToOne;
import com.ctp.cdi.query.test.domain.Parent;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.ParentDao;
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.util.Deployments;

/**
 * 
 * @author thomashug
 */
public class CriteriaTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
        return Deployments.initDeployment()
                          .addPackage(SimpleDao.class.getPackage())
                          .addPackage(Simple.class.getPackage());
    }

    @Inject
    private SimpleDao dao;

    @Inject
    private ParentDao parentDao;

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldCreateCriteriaQuery() {
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
    public void shouldCreateJoinCriteriaQuery() {
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
    public void shouldCreateOrQuery() {
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
    public void shouldCreateOrderedQuery() {
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
    public void shouldCreateQueryWihtoutNulls() {
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
    public void shouldCreateFetchQuery() {
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
    public void shouldCreateInQuery() {
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

    private Simple createSimple(String name, Integer counter) {
        Simple result = new Simple(name);
        result.setCounter(counter);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
