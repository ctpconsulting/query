package com.ctp.cdi.query.builder.result;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.QueryResult;
import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.postprocessor.OrderByQueryStringPostProcessor;
import com.ctp.cdi.query.handler.QueryInvocationContext;

public class DefaultQueryResult<T> implements QueryResult<T> {
    
    private final QueryBuilder builder;
    private final QueryInvocationContext context;

    public DefaultQueryResult(QueryBuilder builder, QueryInvocationContext context) {
        this.builder = builder;
        this.context = context;
    }

    @Override
    public <X> QueryResult<T> orderAsc(SingularAttribute<T, X> order) {
        context.addPostProcessor(new OrderByQueryStringPostProcessor(order, "asc"));
        return this;
    }
    
    @Override
    public <X> QueryResult<T> orderAsc(String order) {
        context.addPostProcessor(new OrderByQueryStringPostProcessor(order, "asc"));
        return this;
    }

    @Override
    public <X> QueryResult<T> orderDesc(SingularAttribute<T, X> order) {
        context.addPostProcessor(new OrderByQueryStringPostProcessor(order, "desc"));
        return this;
    }
    
    @Override
    public <X> QueryResult<T> orderDesc(String order) {
        context.addPostProcessor(new OrderByQueryStringPostProcessor(order, "desc"));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getResultList() {
        try {
            return ((Query) builder.execute(context)).getResultList();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getSingleResult() {
        try {
            return (T) ((Query) builder.execute(context)).getSingleResult();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
