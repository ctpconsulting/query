package org.apache.deltaspike.query.impl;

import static org.junit.Assert.assertNotNull;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.deltaspike.query.test.domain.Simple;
import org.apache.deltaspike.query.test.service.DaoInterface;
import org.apache.deltaspike.query.test.service.ExtendedDaoInterface;
import org.apache.deltaspike.query.test.service.SimpleDao;
import org.apache.deltaspike.query.test.util.TestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;


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
