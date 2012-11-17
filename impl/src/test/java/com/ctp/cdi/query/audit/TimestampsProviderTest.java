package com.ctp.cdi.query.audit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ctp.cdi.query.test.domain.AuditedEntity;
import com.ctp.cdi.query.test.domain.Simple;

public class TimestampsProviderTest {

    @Test
    public void should_set_dates_for_creation() {
        // given
        AuditedEntity entity = new AuditedEntity();
        
        // when
        new TimestampsProvider().prePersist(entity);
        
        // then
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getModified());
        assertNull(entity.getGregorianModified());
        assertNull(entity.getTimestamp());
    }

    @Test
    public void should_set_dates_for_update() {
        // given
        AuditedEntity entity = new AuditedEntity();
        
        // when
        new TimestampsProvider().preUpdate(entity);
        
        // then
        assertNull(entity.getCreated());
        assertNotNull(entity.getModified());
        assertNotNull(entity.getGregorianModified());
        assertNotNull(entity.getTimestamp());
    }

    @Test
    public void should_not_fail_on_non_audited_entity() {
        // given
        Simple entity = new Simple();
        
        // when
        TimestampsProvider provider = new TimestampsProvider();
        provider.prePersist(entity);
        provider.preUpdate(entity);
        
        // then finish the test
    }
    
    @Test(expected = AuditPropertyException.class)
    public void should_fail_on_invalid_entity() {
        // given
        InvalidEntity entity = new InvalidEntity();
        
        // when
        new TimestampsProvider().prePersist(entity);
        
        // then
        fail();
    }
    
    private static class InvalidEntity {
        
        @CreatedOn
        @SuppressWarnings("unused")
        private String nonTemporal;
    
    }

}
