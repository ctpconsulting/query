package com.ctp.cdi.query.util;

import junit.framework.Assert;

import org.junit.Test;

import com.ctp.cdi.query.meta.DaoEntity;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.DaoInterface;
import com.ctp.cdi.query.test.service.ExtendedDaoInterface;
import com.ctp.cdi.query.test.service.SimpleDao;

public class DaoUtilsTest {

    @Test
    public void shouldExtractFromInterface() {
        // when
        DaoEntity result = DaoUtils.extractEntityMetaData(ExtendedDaoInterface.class);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(Simple.class, result.getEntityClass());
        Assert.assertEquals(Long.class, result.getPrimaryClass());
    }

    @Test
    public void shouldExtractFromClass() {
        // when
        DaoEntity result = DaoUtils.extractEntityMetaData(SimpleDao.class);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(Simple.class, result.getEntityClass());
        Assert.assertEquals(Long.class, result.getPrimaryClass());
    }

    @Test
    public void shouldExtractFromAnnotation() {
        // when
        DaoEntity result = DaoUtils.extractEntityMetaData(DaoInterface.class);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(Simple.class, result.getEntityClass());
        Assert.assertEquals(Long.class, result.getPrimaryClass());
    }

}
