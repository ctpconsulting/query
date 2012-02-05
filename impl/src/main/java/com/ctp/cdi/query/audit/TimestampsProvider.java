package com.ctp.cdi.query.audit;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jboss.solder.properties.Property;
import org.jboss.solder.properties.query.AnnotatedPropertyCriteria;
import org.jboss.solder.properties.query.PropertyQueries;
import org.jboss.solder.properties.query.PropertyQuery;

/**
 * Set timestamps on marked properties.
 */
class TimestampsProvider extends AuditProvider {

    private final boolean create;
    private final long systime;

    private TimestampsProvider(Object entity, boolean create) {
        super(entity);
        this.create = create;
        this.systime = System.currentTimeMillis();
    }
    
    public static TimestampsProvider forCreate(Object entity) {
        return new TimestampsProvider(entity, true);
    }
    
    public static TimestampsProvider forUpdate(Object entity) {
        return new TimestampsProvider(entity, false);
    }
    
    public void updateTimestamps() {
        List<Property<Object>> properties = new LinkedList<Property<Object>>();
        PropertyQuery<Object> query = PropertyQueries.<Object>createQuery(entity.getClass())
                .addCriteria(new AnnotatedPropertyCriteria(ModifiedOn.class));
        properties.addAll(query.getWritableResultList());
        if (create) {
            query = PropertyQueries.<Object>createQuery(entity.getClass())
                    .addCriteria(new AnnotatedPropertyCriteria(CreatedOn.class));
            properties.addAll(query.getWritableResultList());
        }
        for (Property<Object> property : properties) {
            setProperty(property);
        }
    }
    
    private void setProperty(Property<Object> property) {
        try {
            if (!isCorrectContext(property))
                return;
            Object now = now(property.getJavaClass());
            property.setValue(entity, now);
            log.debugv("Updated property {0} with {1}", propertyName(property), now);
        } catch (Exception e) {
            String message = "Failed to set property " + propertyName(property) + ", is this a temporal type?";
            throw new AuditPropertyException(message, e);
        }
    }

    private boolean isCorrectContext(Property<Object> property) {
        if (create && property.getAnnotatedElement().isAnnotationPresent(ModifiedOn.class)) {
            ModifiedOn annotation = property.getAnnotatedElement().getAnnotation(ModifiedOn.class);
            if (!annotation.onCreate())
                return false;
        }
        return true;
    }

    private Object now(Class<?> field) throws Exception {
        if (isCalendarClass(field)) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(systime);
            return cal;
        } else if (isDateClass(field)) {
            return field.getConstructor(Long.TYPE).newInstance(systime);
        }
        throw new IllegalArgumentException("Annotated field is not a date class: " + field);
    }

    private boolean isCalendarClass(Class<?> field) {
        return Calendar.class.isAssignableFrom(field);
    }

    private boolean isDateClass(Class<?> field) {
        return Date.class.isAssignableFrom(field);
    }
}
