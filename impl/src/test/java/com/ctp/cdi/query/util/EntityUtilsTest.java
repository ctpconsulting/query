package com.ctp.cdi.query.util;

import java.io.Serializable;

import com.ctp.cdi.query.test.domain.Tee;
import com.ctp.cdi.query.test.domain.Tee2;
import com.ctp.cdi.query.test.domain.TeeId;
import junit.framework.Assert;

import org.junit.Test;

import com.ctp.cdi.query.test.domain.Simple;

public class EntityUtilsTest {

    @Test
    public void shouldDetectNewEntity() {
        // given
        Simple simple = new Simple();

        // when
        boolean isNew = EntityUtils.isNew(simple);

        // then
        Assert.assertTrue(isNew);
    }

    @Test
    public void shouldDetectPersistedEntity() {
        // given
        Simple simple = new Simple();
        simple.setId(Long.valueOf(0));

        // when
        boolean isNew = EntityUtils.isNew(simple);

        // then
        Assert.assertFalse(isNew);
    }

    @Test
    public void shouldFindIdPropertyClass() {
        // given

        // when
        Class<? extends Serializable> pkClass = EntityUtils.primaryKeyClass(Tee.class);

        // then
        Assert.assertEquals(TeeId.class, pkClass);
    }

    @Test
    public void shouldFindIdClass() {
        // given

        // when
        Class<? extends Serializable> pkClass = EntityUtils.primaryKeyClass(Tee2.class);

        // then
        Assert.assertEquals(TeeId.class, pkClass);
    }
}
