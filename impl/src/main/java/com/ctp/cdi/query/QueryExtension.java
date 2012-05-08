package com.ctp.cdi.query;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.solder.bean.BeanBuilder;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.serviceHandler.ServiceHandlerBeanLifecycle;
import org.jboss.solder.serviceHandler.ServiceHandlerExtension;

import com.ctp.cdi.query.handler.QueryHandler;
import com.ctp.cdi.query.meta.DaoComponentsFactory;
import com.ctp.cdi.query.meta.unit.PersistenceUnits;

/**
 * The main extension class for CDI queries, based on Seam Solder service handlers.
 * Overrides the behavior for looking up handler classes.
 *
 * @author thomashug
 */
public class QueryExtension extends ServiceHandlerExtension {

    private final Logger log = Logger.getLogger(QueryExtension.class);
    
    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery before) {
        PersistenceUnits.instance().init();
    }

    @Override
    protected <X> Class<?> getHandlerClass(ProcessAnnotatedType<X> event) {
        if (event.getAnnotatedType().isAnnotationPresent(Dao.class)) {
            log.debugv("getHandlerClass: Dao annotation detected on {0}", event.getAnnotatedType());
            boolean added = DaoComponentsFactory.instance().add(event.getAnnotatedType().getJavaClass());
            if (!added) {
                log.infov("getHandlerClass: Type {0} ignored as it's not related to an entity",
                        event.getAnnotatedType());
            }
            return added ? QueryHandler.class : null;
        }
        return null;
    }
    
    //FIX for https://issues.jboss.org/browse/SOLDER-327
    protected <X> void buildBean(AnnotatedType<X> annotatedType, BeanManager beanManager, final Class<?> handlerClass)
        {
           try
           {
              final BeanBuilder<X> builder = new BeanBuilder<X>(beanManager);
    
              builder.readFromType(annotatedType);
              builder.types(annotatedType.getTypeClosure());
              builder.beanLifecycle(new ServiceHandlerBeanLifecycle(annotatedType.getJavaClass(), handlerClass, beanManager));
              builder.toString("Generated @ServiceHandler for [" + builder.getBeanClass() + "] with qualifiers [" + builder.getQualifiers() +"] handled by " + handlerClass);
              beans.add(builder.create());
              log.debug("Adding @ServiceHandler bean for [" + builder.getBeanClass() + "] with qualifiers [" + builder.getQualifiers() + "] handled by " + handlerClass);
           }
           catch (IllegalArgumentException e)
           {
              throw new RuntimeException(e);
           }
        }


}
