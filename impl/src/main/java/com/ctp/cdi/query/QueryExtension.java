package com.ctp.cdi.query;

import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.serviceHandler.ServiceHandlerExtension;

import com.ctp.cdi.query.handler.QueryHandler;
import com.ctp.cdi.query.meta.DaoComponentsFactory;

public class QueryExtension extends ServiceHandlerExtension {

    private static final Logger log = Logger.getLogger(QueryExtension.class);

    @Override
    protected <X> Class<?> getHandlerClass(ProcessAnnotatedType<X> event) {
        if (event.getAnnotatedType().isAnnotationPresent(Dao.class)) {
            log.debugv("getHandlerClass: Dao annotation detected on {0}", event.getAnnotatedType());
            boolean added = DaoComponentsFactory.instance().add(event.getAnnotatedType().getJavaClass());
            return added ? QueryHandler.class : null;
        }
        return null;
    }

}
