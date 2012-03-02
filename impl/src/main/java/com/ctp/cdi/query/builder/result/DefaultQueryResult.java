package com.ctp.cdi.query.builder.result;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.QueryResult;
import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.postprocessor.FirstResultPostProcessor;
import com.ctp.cdi.query.builder.postprocessor.FlushModePostProcessor;
import com.ctp.cdi.query.builder.postprocessor.HintPostProcessor;
import com.ctp.cdi.query.builder.postprocessor.LockModePostProcessor;
import com.ctp.cdi.query.builder.postprocessor.MaxResultPostProcessor;
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
        context.addQueryStringPostProcessor(new OrderByQueryStringPostProcessor(order, "asc"));
        return this;
    }
    
    @Override
    public QueryResult<T> orderAsc(String order) {
        context.addQueryStringPostProcessor(new OrderByQueryStringPostProcessor(order, "asc"));
        return this;
    }

    @Override
    public <X> QueryResult<T> orderDesc(SingularAttribute<T, X> order) {
        context.addQueryStringPostProcessor(new OrderByQueryStringPostProcessor(order, "desc"));
        return this;
    }
    
    @Override
    public QueryResult<T> orderDesc(String order) {
        context.addQueryStringPostProcessor(new OrderByQueryStringPostProcessor(order, "desc"));
        return this;
    }
    
    @Override
    public QueryResult<T> maxResults(int max) {
        context.addJpaQueryPostProcessor(new MaxResultPostProcessor(max));
        return this;
    }

    @Override
    public QueryResult<T> firstResult(int first) {
        context.addJpaQueryPostProcessor(new FirstResultPostProcessor(first));
        return this;
    }

    @Override
    public QueryResult<T> lockMode(LockModeType lockMode) {
        context.addJpaQueryPostProcessor(new LockModePostProcessor(lockMode));
        return this;
    }

    @Override
    public QueryResult<T> flushMode(FlushModeType flushMode) {
        context.addJpaQueryPostProcessor(new FlushModePostProcessor(flushMode));
        return this;
    }
    
    @Override
    public QueryResult<T> hint(String hint, Object value) {
        context.addJpaQueryPostProcessor(new HintPostProcessor(hint, value));
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
