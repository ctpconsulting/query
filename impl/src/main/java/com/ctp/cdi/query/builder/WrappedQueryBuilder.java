package com.ctp.cdi.query.builder;

import com.ctp.cdi.query.builder.result.DefaultQueryResult;
import com.ctp.cdi.query.handler.QueryInvocationContext;

public class WrappedQueryBuilder extends QueryBuilder {
    
    private final QueryBuilder delegate;

    public WrappedQueryBuilder(QueryBuilder delegate) {
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object execute(QueryInvocationContext ctx) {
        return new DefaultQueryResult(delegate, ctx);
    }

}