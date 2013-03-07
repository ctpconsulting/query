package org.apache.deltaspike.query.impl.audit;

public interface PrePersistAuditListener {

    void prePersist(Object entity);

}
