package com.ctp.cdi.query.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditEntityListener {
    
    @PrePersist
    public void create(Object entity) {
        AuditTimestamps.forCreate(entity).updateTimestamps();
    }
    
    @PreUpdate
    public void update(Object entity) {
        AuditTimestamps.forUpdate(entity).updateTimestamps();
    }

}
