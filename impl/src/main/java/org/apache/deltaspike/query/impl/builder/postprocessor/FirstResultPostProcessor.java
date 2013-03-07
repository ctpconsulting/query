package org.apache.deltaspike.query.impl.builder.postprocessor;

import javax.persistence.Query;

import org.apache.deltaspike.query.impl.handler.CdiQueryInvocationContext;
import org.apache.deltaspike.query.impl.handler.JpaQueryPostProcessor;


public class FirstResultPostProcessor implements JpaQueryPostProcessor {
    
    private final int startPosition;

    public FirstResultPostProcessor(int startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public Query postProcess(CdiQueryInvocationContext context, Query query) {
        query.setFirstResult(startPosition);
        return query;
    }

}
