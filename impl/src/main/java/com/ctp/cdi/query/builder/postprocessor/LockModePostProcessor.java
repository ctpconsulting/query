package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.LockModeType;
import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;
import com.ctp.cdi.query.handler.CdiQueryInvocationContext;

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
