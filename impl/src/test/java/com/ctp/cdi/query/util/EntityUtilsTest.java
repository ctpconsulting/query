package com.ctp.cdi.query.util;

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
}
