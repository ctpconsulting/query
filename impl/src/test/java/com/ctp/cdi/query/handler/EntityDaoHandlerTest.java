package com.ctp.cdi.query.handler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.SingularAttribute;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.domain.Simple_;
import com.ctp.cdi.query.test.service.ExtendedDaoInterface;
import com.ctp.cdi.query.test.util.TestDeployments;

public class EntityDaoHandlerTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
                .addClasses(ExtendedDaoInterface.class)
                .addPackage(Simple.class.getPackage());
    }

    @Inject
    private ExtendedDaoInterface dao;

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldSave() throws Exception {
        // given
        Simple simple = new Simple("test");

        // when
        simple = dao.save(simple);

        // then
        assertNotNull(simple.getId());
    }

    @Test
    public void shouldMerge() throws Exception {
        // given
        Simple simple = createSimple("testMerge");
        Long id = simple.getId();

        // when
        final String newName = "testMergeUpdated";
        simple.setName(newName);
        simple = dao.save(simple);

        // then
        assertEquals(id, simple.getId());
        assertEquals(newName, simple.getName());
    }

    @Test
    public void shouldSaveAndFlush() throws Exception {
        // given
        Simple simple = new Simple("test");

        // when
        simple = dao.saveAndFlush(simple);
        Simple fetch = (Simple) entityManager.createNativeQuery("select * from simple_table where id = ?", Simple.class)
                .setParameter(1, simple.getId())
                .getSingleResult();

        // then
        assertEquals(simple.getId(), fetch.getId());
    }

    @Test
    public void shouldRefresh() throws Exception {
        // given
        final String name = "testRefresh";
        Simple simple = createSimple(name);

        // when
        simple.setName("override");
        dao.refresh(simple);

        // then
        assertEquals(name, simple.getName());
    }

    @Test
    public void shouldFindByPk() throws Exception {
        // given
        Simple simple = createSimple("testFindByPk");

        // when
        Simple find = dao.findBy(simple.getId());

        // then
        assertEquals(simple.getName(), find.getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindByExample() throws Exception {
        // given
        Simple simple = createSimple("testFindByExample");

        // when
        List<Simple> find = dao.findBy(simple, Simple_.name);

        // then
        assertNotNull(find);
        assertFalse(find.isEmpty());
        assertEquals(simple.getName(), find.get(0).getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindByExampleWithStartAndMax() throws Exception {
        // given
        Simple simple = createSimple("testFindByExample1", Integer.valueOf(10));
        createSimple("testFindByExample1", Integer.valueOf(10));

        // when
        List<Simple> find = dao.findBy(simple, 0, 1, Simple_.name, Simple_.counter);

        // then
        assertNotNull(find);
        assertFalse(find.isEmpty());
        assertEquals(1,find.size());
        assertEquals(simple.getName(), find.get(0).getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindByExampleWithNoAttributes() throws Exception {
        // given
        Simple simple = createSimple("testFindByExample");
        SingularAttribute<Simple, ?>[] attributes = new SingularAttribute[] {};

        // when
        List<Simple> find = dao.findBy(simple, attributes);

        // then
        assertNotNull(find);
        assertFalse(find.isEmpty());
        assertEquals(simple.getName(), find.get(0).getName());
    }

    @Test
    public void shouldFindByAll() {
        // given
        createSimple("testFindAll1");
        createSimple("testFindAll2");

        // when
        List<Simple> find = dao.findAll();

        // then
        Assert.assertEquals(2, find.size());
    }

    @Test
    public void shouldFindByAllWithStartAndMax() {
        // given
        createSimple("testFindAll1");
        createSimple("testFindAll2");

        // when
        List<Simple> find = dao.findAll(0, 1);

        // then
        Assert.assertEquals(1, find.size());
    }

    @Test
    public void shouldCountAll() {
        // given
        createSimple("testCountAll");

        // when
        Long result = dao.count();

        // then
        assertEquals(Long.valueOf(1), result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCountWithAttributes() {
        // given
        Simple simple = createSimple("testFindAll1", Integer.valueOf(55));
        createSimple("testFindAll2", Integer.valueOf(55));

        // when
        Long result = dao.count(simple, Simple_.name, Simple_.counter);

        // then
        assertEquals(Long.valueOf(1), result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCountWithNoAttributes() {
        // given
        Simple simple = createSimple("testFindAll1");
        createSimple("testFindAll2");
        SingularAttribute<Simple, Object>[] attributes = new SingularAttribute[] {};

        // when
        Long result = dao.count(simple, attributes);

        // then
        assertEquals(Long.valueOf(2), result);
    }

    @Test
    public void shouldRemove() {
        // given
        Simple simple = createSimple("testRemove");

        // when
        dao.remove(simple);
        dao.flush();
        Simple lookup = entityManager.find(Simple.class, simple.getId());

        // then
        assertNull(lookup);
    }
    
    private Simple createSimple(String name) {
        return createSimple(name, null);
    }

    private Simple createSimple(String name, Integer counter) {
        Simple result = new Simple(name);
        result.setCounter(counter);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
