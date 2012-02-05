package com.ctp.cdi.query.audit;

import org.jboss.solder.logging.Logger;
import org.jboss.solder.properties.Property;

abstract class AuditProvider {

    final Logger log = Logger.getLogger(getClass());
    
    final Object entity;
    
    AuditProvider(Object entity) {
        this.entity = entity;
    }
    
    String propertyName(Property<Object> property) {
        return entity.getClass().getSimpleName() + "." + property.getName();
    }
}
