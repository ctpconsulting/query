package org.apache.deltaspike.query.impl.builder.postprocessor;

import javax.persistence.Query;

import org.apache.deltaspike.query.impl.handler.CdiQueryInvocationContext;
import org.apache.deltaspike.query.impl.handler.JpaQueryPostProcessor;


public class HintPostProcessor implements JpaQueryPostProcessor {

    private final String hintName;
    private final Object hintValue;

    public HintPostProcessor(String hintName, Object hintValue) {
        this.hintName = hintName;
        this.hintValue = hintValue;
    }

    @Override
    public Query postProcess(CdiQueryInvocationContext context, Query query) {
        query.setHint(hintName, hintValue);
        return query;
    }

}
