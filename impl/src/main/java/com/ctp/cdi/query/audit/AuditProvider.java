package com.ctp.cdi.query.audit;

import org.jboss.solder.logging.Logger;
import org.jboss.solder.properties.Property;

abstract class AuditProvider implements PrePersistAuditListener, PreUpdateAuditListener {

    final Logger log = Logger.getLogger(getClass());
    
    String propertyName(Object entity, Property<Object> property) {
        return entity.getClass().getSimpleName() + "." + property.getName();
    }

}
