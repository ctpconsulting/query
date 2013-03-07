package org.apache.deltaspike.query.impl.audit;

import java.util.logging.Logger;

import org.apache.deltaspike.query.impl.property.Property;


abstract class AuditProvider implements PrePersistAuditListener, PreUpdateAuditListener {

    protected static final Logger log = Logger.getLogger(AuditProvider.class.getName());

    String propertyName(Object entity, Property<Object> property) {
        return entity.getClass().getSimpleName() + "." + property.getName();
    }

}
