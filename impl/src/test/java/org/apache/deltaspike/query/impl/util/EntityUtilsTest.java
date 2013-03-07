package org.apache.deltaspike.query.impl.util;

import java.io.Serializable;

import org.apache.deltaspike.query.impl.util.EntityUtils;
import org.apache.deltaspike.query.test.domain.Tee;
import org.apache.deltaspike.query.test.domain.Tee2;
import org.apache.deltaspike.query.test.domain.TeeId;
import org.junit.Assert;

import org.junit.Test;


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
