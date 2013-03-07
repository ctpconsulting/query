package org.apache.deltaspike.query.impl.builder.postprocessor;

import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.apache.deltaspike.query.impl.handler.CdiQueryInvocationContext;
import org.apache.deltaspike.query.impl.handler.JpaQueryPostProcessor;


public class LockModePostProcessor implements JpaQueryPostProcessor {
    
    private final LockModeType lockMode;

    public LockModePostProcessor(LockModeType lockMode) {
        this.lockMode = lockMode;
    }

    @Override
    public Query postProcess(CdiQueryInvocationContext context, Query query) {
        query.setLockMode(lockMode);
        return query;
    }

}
