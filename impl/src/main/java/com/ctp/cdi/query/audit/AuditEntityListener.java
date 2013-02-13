package com.ctp.cdi.query.audit;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;

public class AuditEntityListener {

    @PrePersist
    public void persist(Object entity) {
        BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(PrePersistAuditListener.class);
        for (Bean<?> bean : beans) {
            PrePersistAuditListener result = (PrePersistAuditListener) beanManager.getReference(
                    bean, PrePersistAuditListener.class, beanManager.createCreationalContext(bean));
            result.prePersist(entity);
        }
    }

    @PreUpdate
    public void update(Object entity) {
        BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(PreUpdateAuditListener.class);
        for (Bean<?> bean : beans) {
            PreUpdateAuditListener result = (PreUpdateAuditListener) beanManager.getReference(
                    bean, PreUpdateAuditListener.class, beanManager.createCreationalContext(bean));
            result.preUpdate(entity);
        }
    }

}
