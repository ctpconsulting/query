package com.ctp.cdi.query.util;

import java.io.Serializable;

import junit.framework.Assert;

import org.junit.Test;

import com.ctp.cdi.query.test.domain.Tee;
import com.ctp.cdi.query.test.domain.Tee2;
import com.ctp.cdi.query.test.domain.TeeId;

public class EntityUtilsTest {

    @Test
    public void should_find_id_property_class() {
        // given

        // when
        Class<? extends Serializable> pkClass = EntityUtils.primaryKeyClass(Tee.class);

        // then
        Assert.assertEquals(TeeId.class, pkClass);
    }

    @Test
    public void should_find_id_class() {
        // given

        // when
        Class<? extends Serializable> pkClass = EntityUtils.primaryKeyClass(Tee2.class);

        // then
        Assert.assertEquals(TeeId.class, pkClass);
    }
}
