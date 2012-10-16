package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.FlushModeType;
import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;
import com.ctp.cdi.query.handler.CdiQueryInvocationContext;

public class FlushModePostProcessor implements JpaQueryPostProcessor {

    private final FlushModeType flushMode;

    public FlushModePostProcessor(FlushModeType flushMode) {
        this.flushMode = flushMode;
    }

    @Override
    public Query postProcess(CdiQueryInvocationContext context, Query query) {
        query.setFlushMode(flushMode);
        return query;
    }

}
