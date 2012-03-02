package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;

public class FirstResultPostProcessor implements JpaQueryPostProcessor {
    
    private final int startPosition;

    public FirstResultPostProcessor(int startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public void postProcess(Query query) {
        query.setFirstResult(startPosition);
    }

}
