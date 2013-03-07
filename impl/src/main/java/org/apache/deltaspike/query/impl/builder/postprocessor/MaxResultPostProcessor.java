package org.apache.deltaspike.query.impl.builder.postprocessor;

import javax.persistence.Query;

import org.apache.deltaspike.query.impl.handler.CdiQueryInvocationContext;
import org.apache.deltaspike.query.impl.handler.JpaQueryPostProcessor;


public class MaxResultPostProcessor implements JpaQueryPostProcessor {
    
    private final int max;

    public MaxResultPostProcessor(int max) {
        this.max = max;
    }

    @Override
    public Query postProcess(CdiQueryInvocationContext context, Query query) {
        query.setMaxResults(max);
        return query;
    }

}
