package com.ctp.cdi.query.handler;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.ExtendedAbstractEntityDao;
import com.ctp.cdi.query.test.util.Deployments;
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

@RunWith(Arquillian.class)
public class QueryHandlerTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deployment() {
        return Deployments.initDeployment()
                .addClasses(ExtendedAbstractEntityDao.class)
                .addPackage(Simple.class.getPackage());
    }
    
    @Inject
    private ExtendedAbstractEntityDao dao;
    
    @Produces
    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    public void shouldDelegateToImplementation() {
        // given
        final String name = "testDelegateToImplementation";
        Simple simple = createSimple(name);
        
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
        Simple simple = createSimple(name);
        
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
        Simple simple = createSimple(name);
        
        // when
        Simple result = dao.findByQuery(name);
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(name, result.getName());
    }
    
    private Simple createSimple(String name) {
        Simple result = new Simple(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }
    
}
