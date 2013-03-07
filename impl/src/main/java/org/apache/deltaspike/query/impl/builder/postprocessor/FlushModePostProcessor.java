package org.apache.deltaspike.query.impl.builder.postprocessor;

import javax.persistence.FlushModeType;
import javax.persistence.Query;

import org.apache.deltaspike.query.impl.handler.CdiQueryInvocationContext;
import org.apache.deltaspike.query.impl.handler.JpaQueryPostProcessor;


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
