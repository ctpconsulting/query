package com.ctp.cdi.query.audit;

import java.util.logging.Logger;

import com.ctp.cdi.query.property.Property;

abstract class AuditProvider implements PrePersistAuditListener, PreUpdateAuditListener {

    protected static final Logger log = Logger.getLogger(AuditProvider.class.getName());

    String propertyName(Object entity, Property<Object> property) {
        return entity.getClass().getSimpleName() + "." + property.getName();
    }

}
