package org.apache.deltaspike.query.impl.builder;

import java.io.Serializable;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.deltaspike.query.api.QueryResult;
import org.apache.deltaspike.query.impl.meta.DaoMethod;
import org.apache.deltaspike.query.impl.meta.QueryInvocationLiteral;


public class QueryBuilderFactory implements Serializable {

    private static final long serialVersionUID = 1L;

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