package com.ctp.cdi.query.handler;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.util.Deployments;

@RunWith(Arquillian.class)
public class QueryHandlerTest extends TransactionalTestCase {
    
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
    
    @Test
    public void shouldDelegateToImplementation() {
        // given
        final String name = "testDelegateToImplementation";
        createSimple(name);
        
        // when
        List<Simple> result = dao.implementedQueryByName(name);
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
    }
    
    @Test
    public void shouldCreateNamedQueryIndex() {
        // given
        final String name = "testCreateNamedQueryIndex";
        createSimple(name);
        
        // when
        List<Simple> result = dao.findByNamedQueryIndexed(name, Boolean.TRUE);
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(name, result.get(0).getName());
    }
    
    @Test
    public void shouldCreateNamedQueryNamed() {
        // given
        final String name = "testCreateNamedQueryNamed";
        Simple simple = createSimple(name);
        
        // when
        Simple result = dao.findByNamedQueryNamed(simple.getId(), Boolean.TRUE);
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(name, result.getName());
    }
    
    @Test
    public void shouldRunAnnotatedQuery() {
        // given
        final String name = "testRunAnnotatedQuery";
        createSimple(name);
        
        // when
        Simple result = dao.findByQuery(name);
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(name, result.getName());
    }
    
    @Test
    public void shouldCreateQueryByMethodName() {
        // given
        final String name = "testCreateQueryByMethodName";
        createSimple(name);
        
        // when
        Simple result = dao.findByNameAndEnabled(name, Boolean.TRUE);
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(name, result.getName());
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
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
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
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(second.getId(), result.get(0).getId());
    }

    private Simple createSimple(String name) {
        Simple result = new Simple(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }
    
}
