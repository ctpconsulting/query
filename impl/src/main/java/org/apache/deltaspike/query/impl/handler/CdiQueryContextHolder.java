package org.apache.deltaspike.query.impl.handler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class CdiQueryContextHolder {

    private final ThreadLocal<CdiQueryInvocationContext> context = new ThreadLocal<CdiQueryInvocationContext>();

    public void set(CdiQueryInvocationContext context) {
        this.context.set(context);
    }

    @Produces
    public CdiQueryInvocationContext get() {
        return context.get();
    }

    public void dispose() {
        context.remove();
    }

}
