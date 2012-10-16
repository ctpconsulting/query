package com.ctp.cdi.query.handler;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@RequestScoped
public class CdiQueryContextFactory {

    @Produces
    private CdiQueryInvocationContext context;

    public void contextCreated(@Observes CdiQueryInvocationContext context) {
        this.context = context;
    }

}
