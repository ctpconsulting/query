package org.apache.deltaspike.query.impl.audit;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.apache.deltaspike.query.api.audit.CreatedOn;
import org.apache.deltaspike.query.api.audit.ModifiedOn;
import org.apache.deltaspike.query.impl.property.Property;
import org.apache.deltaspike.query.impl.property.query.AnnotatedPropertyCriteria;
import org.apache.deltaspike.query.impl.property.query.PropertyQueries;
import org.apache.deltaspike.query.impl.property.query.PropertyQuery;


/**
 * Set timestamps on marked properties.
 */
class TimestampsProvider extends AuditProvider {

    @Override
    public void prePersist(Object entity) {
        updateTimestamps(entity, true);
    }

    @Override
    public void preUpdate(Object entity) {
        updateTimestamps(entity, false);
    }

    private void updateTimestamps(Object entity, boolean create) {
        long systime = System.currentTimeMillis();
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
            setProperty(entity, property, systime, create);
        }
    }

    private void setProperty(Object entity, Property<Object> property, long systime, boolean create) {
        try {
            if (!isCorrectContext(property, create)) {
                return;
            }
            Object now = now(property.getJavaClass(), systime);
            property.setValue(entity, now);
            log.log(Level.FINER, "Updated property {0} with {1}", new Object[] { propertyName(entity, property), now });
        } catch (Exception e) {
            String message = "Failed to set property " + propertyName(entity, property) + ", is this a temporal type?";
            throw new AuditPropertyException(message, e);
        }
    }

    private boolean isCorrectContext(Property<Object> property, boolean create) {
        if (create && property.getAnnotatedElement().isAnnotationPresent(ModifiedOn.class)) {
            ModifiedOn annotation = property.getAnnotatedElement().getAnnotation(ModifiedOn.class);
            if (!annotation.onCreate()) {
                return false;
            }
        }
        return true;
    }

    private Object now(Class<?> field, long systime) throws Exception {
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
