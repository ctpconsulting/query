package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;

public class MaxResultPostProcessor implements JpaQueryPostProcessor {
    
    private final int max;

    public MaxResultPostProcessor(int max) {
        this.max = max;
    }

    @Override
    public void postProcess(Query query) {
        query.setMaxResults(max);
    }

}
