package com.ctp.cdi.query.audit;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

import org.junit.Test;

import com.ctp.cdi.query.test.domain.AuditedEntity;
import com.ctp.cdi.query.test.domain.Simple;

public class TimestampsProviderTest {

    @Test
    public void shouldSetDatesForCreation() {
        // given
        AuditedEntity entity = new AuditedEntity();
        
        // when
        new TimestampsProvider().prePersist(entity);
        
        // then
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getModified());
        assertNull(entity.getGregorianModified());
        assertNull(entity.getSqlModified());
        assertNull(entity.getTimestamp());
    }

    @Test
    public void shouldSetDatesForUpdate() {
        // given
        AuditedEntity entity = new AuditedEntity();
        
        // when
        new TimestampsProvider().preUpdate(entity);
        
        // then
        assertNull(entity.getCreated());
        assertNotNull(entity.getModified());
        assertNotNull(entity.getGregorianModified());
        assertNotNull(entity.getSqlModified());
        assertNotNull(entity.getTimestamp());
    }

    @Test
    public void shouldNotFailOnNonAuditedEntity() {
        // given
        Simple entity = new Simple();
        
        // when
        TimestampsProvider provider = new TimestampsProvider();
        provider.prePersist(entity);
        provider.preUpdate(entity);
        
        // then finish the test
    }
    
    @Test(expected = AuditPropertyException.class)
    public void shouldFailOnInvalidEntity() {
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
