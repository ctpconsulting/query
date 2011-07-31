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

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.domain.Simple_;
import com.ctp.cdi.query.test.service.ExtendedDaoInterface;
import com.ctp.cdi.query.test.util.Deployments;

public class EntityDaoHandlerTest extends TransactionalTestCase {

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
        Simple fetch = (Simple) entityManager.createNativeQuery("select * from simple where id = ?", Simple.class)
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
    public void shouldCountAll() {
        // given
        createSimple("testCountAll");
        
        // when
        Long result = dao.count();
        
        // then
        assertEquals(Long.valueOf(1), result);
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
        Simple result = new Simple(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
