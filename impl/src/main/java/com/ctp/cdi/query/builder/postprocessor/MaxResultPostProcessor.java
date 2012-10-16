package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;
import com.ctp.cdi.query.handler.CdiQueryInvocationContext;

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
