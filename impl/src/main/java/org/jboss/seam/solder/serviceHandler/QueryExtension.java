package org.jboss.seam.solder.serviceHandler;

import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.logging.Logger;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.handler.QueryHandler;

public class QueryExtension extends ServiceHandlerExtension {

    private static final Logger log = Logger.getLogger(QueryExtension.class);

    @Override
    protected <X> Class<?> getHandlerClass(ProcessAnnotatedType<X> event) {
	if (event.getAnnotatedType().isAnnotationPresent(Dao.class)) {
	    log.debug("Received handler class request2: " + event.getAnnotatedType());
	    return QueryHandler.class;
	}
	return null;
    }

}
