package com.ctp.cdi.query;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.DaoInterface;
import com.ctp.cdi.query.test.service.ExtendedAbstractEntityDao;
import com.ctp.cdi.query.test.service.ExtendedDaoInterface;
import com.ctp.cdi.query.test.util.Deployments;


@RunWith(Arquillian.class)
public class QueryExtensionTest {
    
    @Deployment
    public static Archive<?> deployment() {
	return Deployments.initDeployment()
		.addPackage(DaoInterface.class.getPackage())
		.addPackage(Simple.class.getPackage());
    }

    @Inject
    Instance<DaoInterface> dao;
    
    @Inject
    Instance<ExtendedDaoInterface> extendedDao;
    
    @Inject
    Instance<ExtendedAbstractEntityDao> extendedClassDao;

    @Test
    public void shouldInject() {
	Assert.assertNotNull(dao.get());
	Assert.assertNotNull(extendedDao.get());
	Assert.assertNotNull(extendedClassDao.get());
    }

}
