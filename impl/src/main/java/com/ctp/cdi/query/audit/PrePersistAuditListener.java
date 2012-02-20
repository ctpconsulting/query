package com.ctp.cdi.query.audit;

public interface PrePersistAuditListener {

    void prePersist(Object entity);

}
