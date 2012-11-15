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
