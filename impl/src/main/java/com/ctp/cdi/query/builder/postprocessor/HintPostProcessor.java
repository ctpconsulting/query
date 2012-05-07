package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;
import com.ctp.cdi.query.handler.QueryInvocationContext;

public class HintPostProcessor implements JpaQueryPostProcessor {

    private final String hintName;
    private final Object hintValue;

    public HintPostProcessor(String hintName, Object hintValue) {
        this.hintName = hintName;
        this.hintValue = hintValue;
    }

    @Override
    public Query postProcess(QueryInvocationContext context, Query query) {
        query.setHint(hintName, hintValue);
        return query;
    }

}
