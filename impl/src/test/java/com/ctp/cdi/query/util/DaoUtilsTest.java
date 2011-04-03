package com.ctp.cdi.query.util;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.handler.DaoMetaData;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.DaoInterface;
import com.ctp.cdi.query.test.service.ExtendedAbstractEntityDao;
import com.ctp.cdi.query.test.service.ExtendedDaoInterface;
import com.ctp.cdi.query.test.util.Deployments;

@RunWith(Arquillian.class)
public class DaoUtilsTest {
    
    @Deployment
    public static Archive<?> deployment() {
        return Deployments.initDeployment()
                .addPackage(DaoInterface.class.getPackage())
                .addPackage(EntityDao.class.getPackage())
                .addPackage(Simple.class.getPackage());
    }
    
    @Inject
    private DaoInterface daoInterFace;
    
    @Inject
    private ExtendedDaoInterface interFace;
    
    @Inject
    private ExtendedAbstractEntityDao abstractClass;

    @Test
    public void shouldExtractFromInterface() {
        // when
        DaoMetaData result = DaoUtils.extractEntityMetaData(interFace.getClass());
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(Simple.class, result.getEntityClass());
        Assert.assertEquals(Long.class, result.getPrimaryClass());
    }
    
    @Test
    public void shouldExtractFromClass() {
        // when
        DaoMetaData result = DaoUtils.extractEntityMetaData(abstractClass.getClass());
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(Simple.class, result.getEntityClass());
        Assert.assertEquals(Long.class, result.getPrimaryClass());
    }
    
    @Test
    public void shouldExtractFromAnnotation() {
        // when
        DaoMetaData result = DaoUtils.extractEntityMetaData(daoInterFace.getClass());
        
        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(Simple.class, result.getEntityClass());
        Assert.assertEquals(Long.class, result.getPrimaryClass());
    }

}
