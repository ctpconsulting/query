package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.LockModeType;
import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;
import com.ctp.cdi.query.handler.QueryInvocationContext;

public class LockModePostProcessor implements JpaQueryPostProcessor {
    
    private final LockModeType lockMode;

    public LockModePostProcessor(LockModeType lockMode) {
        this.lockMode = lockMode;
    }

    @Override
    public Query postProcess(QueryInvocationContext context, Query query) {
        query.setLockMode(lockMode);
        return query;
    }

}
