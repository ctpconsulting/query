package com.ctp.cdi.query;

import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.serviceHandler.ServiceHandlerExtension;

import com.ctp.cdi.query.handler.QueryHandler;

public class QueryExtension extends ServiceHandlerExtension {

    private static final Logger log = Logger.getLogger(QueryExtension.class);

    @Override
    protected <X> Class<?> getHandlerClass(ProcessAnnotatedType<X> event) {
        if (event.getAnnotatedType().isAnnotationPresent(Dao.class)) {
            log.debugv("getHandlerClass: Dao annotation detected on {0}", event.getAnnotatedType());
            // TODO:
            // - Validate the annotated type
            // - Preprocess. We can build up all the metadata before.
            return QueryHandler.class;
        }
        return null;
    }

}
