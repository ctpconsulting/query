package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.LockModeType;
import javax.persistence.Query;

import com.ctp.cdi.query.handler.JpaQueryPostProcessor;

public class LockModePostProcessor implements JpaQueryPostProcessor {
    
    private final LockModeType lockMode;

    public LockModePostProcessor(LockModeType lockMode) {
        this.lockMode = lockMode;
    }

    @Override
    public void postProcess(Query query) {
        query.setLockMode(lockMode);
    }

}
