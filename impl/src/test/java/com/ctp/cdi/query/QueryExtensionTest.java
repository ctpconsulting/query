package com.ctp.cdi.query;

import static org.junit.Assert.assertNotNull;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.DaoInterface;
import com.ctp.cdi.query.test.service.ExtendedDaoInterface;
import com.ctp.cdi.query.test.service.SimpleDao;
import com.ctp.cdi.query.test.util.TestDeployments;

@RunWith(Arquillian.class)
public class QueryExtensionTest {

    @Deployment(order = 2)
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
                          .addPackage(DaoInterface.class.getPackage())
                          .addPackages(true, Simple.class.getPackage());
    }

    @Inject
    Instance<DaoInterface> dao;

    @Inject
    Instance<ExtendedDaoInterface> extendedDao;

    @Inject
    Instance<SimpleDao> extendedClassDao;

    @Test
    public void should_inject() {
        assertNotNull(dao.get());
        assertNotNull(extendedDao.get());
        assertNotNull(extendedClassDao.get());
    }

}
