package org.apache.deltaspike.query.impl.builder;

import org.apache.deltaspike.query.impl.builder.result.DefaultQueryResult;
import org.apache.deltaspike.query.impl.handler.CdiQueryInvocationContext;

public class WrappedQueryBuilder extends QueryBuilder {
    
    private final QueryBuilder delegate;

    public WrappedQueryBuilder(QueryBuilder delegate) {
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object execute(CdiQueryInvocationContext ctx) {
        return new DefaultQueryResult(delegate, ctx);
    }

}