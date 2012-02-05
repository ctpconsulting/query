package com.ctp.cdi.query.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditEntityListener {
    
    @PrePersist
    public void create(Object entity) {
        TimestampsProvider.forCreate(entity).updateTimestamps();
        PrincipalProvider.forCreateAndUpdate(entity).updatePrincipal();
    }
    
    @PreUpdate
    public void update(Object entity) {
        TimestampsProvider.forUpdate(entity).updateTimestamps();
        PrincipalProvider.forCreateAndUpdate(entity).updatePrincipal();
    }

}
