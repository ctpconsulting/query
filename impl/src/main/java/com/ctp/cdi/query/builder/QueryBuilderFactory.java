package com.ctp.cdi.query.builder;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.ctp.cdi.query.QueryResult;
import com.ctp.cdi.query.meta.DaoMethod;
import com.ctp.cdi.query.meta.QueryInvocationLiteral;

public class QueryBuilderFactory {

    @Inject @Any
    private Instance<QueryBuilder> queryBuilder;
    
    public QueryBuilder build(DaoMethod method) {
        QueryBuilder builder = queryBuilder.select(new QueryInvocationLiteral(method.getMethodType())).get();
        if (method.returns(QueryResult.class)) {
            return new WrappedQueryBuilder(builder);
        }
        return builder;
    }
    
}