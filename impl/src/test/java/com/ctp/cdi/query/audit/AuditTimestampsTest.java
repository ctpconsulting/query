package com.ctp.cdi.query.audit;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

import com.ctp.cdi.query.test.domain.AuditedEntity;
import com.ctp.cdi.query.test.domain.Simple;

public class AuditTimestampsTest {

    @Test
    public void shouldSetDatesForCreation() {
        // given
        AuditedEntity entity = new AuditedEntity();
        
        // when
        AuditTimestamps.forCreate(entity).updateTimestamps();
        
        // then
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getModified());
        assertNull(entity.getGregorianModified());
        assertNull(entity.getSqlModified());
        assertNull(entity.getDecoy());
        assertNull(entity.getTimestamp());
    }

    @Test
    public void shouldSetDatesForUpdate() {
        // given
        AuditedEntity entity = new AuditedEntity();
        
        // when
        AuditTimestamps.forUpdate(entity).updateTimestamps();
        
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
        AuditTimestamps.forCreate(entity).updateTimestamps();
        AuditTimestamps.forUpdate(entity).updateTimestamps();
        
        // then finish the test
    }

}
