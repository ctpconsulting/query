package com.ctp.cdi.query;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.DaoInterface;
import com.ctp.cdi.query.test.service.ExtendedDaoInterface;
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.util.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class QueryExtensionTest {

    @Deployment(order = 2)
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
    Instance<SimpleDao> extendedClassDao;

    @Test
    public void shouldInject() {
        assertNotNull(dao.get());
        assertNotNull(extendedDao.get());
        assertNotNull(extendedClassDao.get());
    }

}
