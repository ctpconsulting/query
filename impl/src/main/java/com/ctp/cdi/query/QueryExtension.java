package com.ctp.cdi.query;

import java.lang.reflect.InvocationHandler;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.solder.logging.Logger;

import com.ctp.cdi.query.meta.DaoComponentsFactory;
import com.ctp.cdi.query.meta.unit.PersistenceUnits;

/**
 * The main extension class for CDI queries, based on Seam Solder service handlers.
 * Overrides the behavior for looking up handler classes.
 *
 * @author thomashug
 */
public class QueryExtension implements Extension {

    private final Logger log = Logger.getLogger(QueryExtension.class);

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery before) {
        PersistenceUnits.instance().init();
    }

    <X> void processAnnotatedType(@Observes ProcessAnnotatedType<X> event, BeanManager beanManager) {
        if (isDao(event.getAnnotatedType())) {
            log.debugv("getHandlerClass: Dao annotation detected on {0}", event.getAnnotatedType());
            boolean added = DaoComponentsFactory.instance().add(event.getAnnotatedType().getJavaClass());
            if (!added) {
                log.infov("getHandlerClass: Type {0} ignored as it's not related to an entity",
                        event.getAnnotatedType());
            }
        }
    }

    private <X> boolean isDao(AnnotatedType<X> annotatedType) {
        return (annotatedType.isAnnotationPresent(Dao.class) ||
                annotatedType.getJavaClass().isAnnotationPresent(Dao.class)) &&
                !InvocationHandler.class.isAssignableFrom(annotatedType.getJavaClass());
    }

}
