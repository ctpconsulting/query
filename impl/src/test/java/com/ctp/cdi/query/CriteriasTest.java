/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctp.cdi.query;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.util.Deployments;
import java.util.List;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import junit.framework.Assert;
import org.jboss.arquillian.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;

/**
 *
 * @author thomashug
 */
public class CriteriasTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deployment() {
	return Deployments.initDeployment()
		.addPackage(SimpleDao.class.getPackage())
		.addPackage(Simple.class.getPackage());
    }

    @Inject
    SimpleDao dao;
    
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
        Assert.assertEquals(0, result1.size());
        Assert.assertEquals(1, result2.size());
        Assert.assertEquals(0, result3.size());
    }
    
    private Simple createSimple(String name, Integer counter) {
        Simple result = new Simple(name);
        result.setCounter(counter);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }
    
}
