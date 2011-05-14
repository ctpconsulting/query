package com.ctp.cdi.query;

import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.serviceHandler.ServiceHandlerExtension;

import com.ctp.cdi.query.handler.QueryHandler;
import com.ctp.cdi.query.meta.DaoComponentsFactory;

/**
 * The main extension class for CDI queries, based on Seam Solder service handlers.
 * Overrides the behavior for looking up handler classes.
 * 
 * @author thomashug
 */
public class QueryExtension extends ServiceHandlerExtension {

    private final Logger log = Logger.getLogger(QueryExtension.class);

    @Override
    protected <X> Class<?> getHandlerClass(ProcessAnnotatedType<X> event) {
        if (event.getAnnotatedType().isAnnotationPresent(Dao.class)) {
            log.debugv("getHandlerClass: Dao annotation detected on {0}", event.getAnnotatedType());
            // TODO validate if java class is an entity class (@Dao(Glass.class))
            boolean added = DaoComponentsFactory.instance().add(event.getAnnotatedType().getJavaClass());
            if (!added) {
                log.infov("getHandlerClass: Type {0} ignored as it's not related to an entity", 
                        event.getAnnotatedType());
            }
            return added ? QueryHandler.class : null;
        }
        return null;
    }

}
