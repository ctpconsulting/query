package com.ctp.cdi.query.audit;

import java.util.Set;
import java.util.logging.Level;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;

import com.ctp.cdi.query.property.Property;
import com.ctp.cdi.query.property.query.AnnotatedPropertyCriteria;
import com.ctp.cdi.query.property.query.PropertyQueries;
import com.ctp.cdi.query.property.query.PropertyQuery;

class PrincipalProvider extends AuditProvider {

    @Inject
    private BeanManager manager;

    @Override
    public void prePersist(Object entity) {
        updatePrincipal(entity);
    }

    @Override
    public void preUpdate(Object entity) {
        updatePrincipal(entity);
    }

    private void updatePrincipal(Object entity) {
        PropertyQuery<Object> query = PropertyQueries.<Object>createQuery(entity.getClass())
                .addCriteria(new AnnotatedPropertyCriteria(ModifiedBy.class));
        for (Property<Object> property : query.getWritableResultList()) {
            setProperty(entity, property);
        }
    }

    private void setProperty(Object entity, Property<Object> property) {
        try {
            Object value = resolvePrincipal(entity, property);
            property.setValue(entity, value);
            log.log(Level.FINER, "Updated {0} with {1}", new Object[] {propertyName(entity, property), value});
        } catch (Exception e) {
            throw new AuditPropertyException("Failed to write principal to " +
                    propertyName(entity, property), e);
        }
    }

    private Object resolvePrincipal(Object entity, Property<Object> property) {
        CurrentUser principal = AnnotationInstanceProvider.of(CurrentUser.class);
        Class<?> propertyClass = property.getJavaClass();
        Set<Bean<?>> beans = manager.getBeans(propertyClass, principal);
        if (!beans.isEmpty() && beans.size() == 1) {
            Bean<?> bean = beans.iterator().next();
            Object result = manager.getReference(bean, propertyClass, manager.createCreationalContext(bean));
            return result;
        }
        throw new IllegalArgumentException("Principal " + (beans.isEmpty() ? "not found" : "not unique") +
                " for " + propertyName(entity, property));
    }

}
