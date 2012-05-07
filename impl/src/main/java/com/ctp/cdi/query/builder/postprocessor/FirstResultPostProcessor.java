package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;
import com.ctp.cdi.query.handler.QueryInvocationContext;

public class FirstResultPostProcessor implements JpaQueryPostProcessor {
    
    private final int startPosition;

    public FirstResultPostProcessor(int startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public Query postProcess(QueryInvocationContext context, Query query) {
        query.setFirstResult(startPosition);
        return query;
    }

}
