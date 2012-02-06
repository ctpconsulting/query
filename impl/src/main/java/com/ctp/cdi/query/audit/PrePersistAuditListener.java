package com.ctp.cdi.query.audit;

public interface PrePersistAuditListener {

    public void prePersist(Object entity);

}
