package com.ctp.cdi.query.home;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Home;
import com.ctp.cdi.query.test.service.HomeDao;
import com.ctp.cdi.query.test.service.HomeEntityHome;
import com.ctp.cdi.query.test.util.TestDeployments;

public class EntityHomeTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
                .addClasses(HomeEntityHome.class, HomeDao.class)
                .addPackage(Home.class.getPackage());
    }

    @Inject
    private HomeEntityHome home;
    
    @Produces
    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    public void shouldRetrieve() {
        // given
        Home entity = new Home();
        entity.setName("testRetrieve");
        home.getEntityDao().save(entity);
        
        // when
        home.setId(entity.getId());
        home.retrieve();
        
        // then
        assertNotNull(home.getEntity());
    }
    
    @Test
    public void shouldCreate() {
        // given
        Home entity = new Home();
        entity.setName("testCreate");
        home.setEntity(entity);
        
        // when
        home.update();
        
        // then
        assertNotNull(home.getEntity().getId());
    }
    
    @Test
    public void shouldUpdate() {
        // given
        Home entity = new Home();
        entity.setName("testUpdate");
        home.getEntityDao().save(entity);
        home.setEntity(entity);
        entity.setName("testUpdate_updated");
        
        // when
        home.update();
        home.setId(entity.getId());
        home.setEntity(null);
        home.retrieve();
        
        // then
        assertEquals("testUpdate_updated", home.getEntity().getName());
    }

    @Test
    public void shouldDelete() {
        // given
        Home entity = new Home();
        entity.setName("testDelete");
        home.getEntityDao().save(entity);
        home.setId(entity.getId());
        
        // when
        home.delete();
        
        // then
        assertNull(home.getEntityDao().findBy(entity.getId()));
    }
    
    @Test
    public void shouldPaginate() {
        // given
        String name = "testPaginate";
        createHome(name);
        createHome(name);
        createHome(name);
        createHome(name);
        home.getSearch().setName(name);
        home.setPage(1);
        home.setPageSize(2);
        
        // when
        home.paginate();
        
        // then
        assertNotNull(home.getPageItems());
        assertEquals(2, home.getPageItems().size());
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    private Home createHome(String name) {
        Home entity = new Home();
        entity.setName("testPaginate");
        entityManager.persist(entity);
        return entity;
    }
}
