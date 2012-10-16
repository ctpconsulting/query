package com.ctp.cdi.query.builder.result;

import java.util.Iterator;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.QueryResult;
import com.ctp.cdi.query.builder.OrderDirection;
import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.postprocessor.CountQueryPostProcessor;
import com.ctp.cdi.query.builder.postprocessor.FirstResultPostProcessor;
import com.ctp.cdi.query.builder.postprocessor.FlushModePostProcessor;
import com.ctp.cdi.query.builder.postprocessor.HintPostProcessor;
import com.ctp.cdi.query.builder.postprocessor.LockModePostProcessor;
import com.ctp.cdi.query.builder.postprocessor.MaxResultPostProcessor;
import com.ctp.cdi.query.builder.postprocessor.OrderByQueryStringPostProcessor;
import com.ctp.cdi.query.handler.CdiQueryInvocationContext;
import com.ctp.cdi.query.handler.QueryStringPostProcessor;

public class DefaultQueryResult<T> implements QueryResult<T> {
    
    private final QueryBuilder builder;
    private final CdiQueryInvocationContext context;
    
    private int page = 0;
    private int pageSize = 10;

    public DefaultQueryResult(QueryBuilder builder, CdiQueryInvocationContext context) {
        this.builder = builder;
        this.context = context;
    }

    @Override
    public <X> QueryResult<T> orderAsc(SingularAttribute<T, X> attribute) {
        context.addQueryStringPostProcessor(new OrderByQueryStringPostProcessor(attribute, OrderDirection.ASC));
        return this;
    }
    
    @Override
    public QueryResult<T> orderAsc(String attribute) {
        context.addQueryStringPostProcessor(new OrderByQueryStringPostProcessor(attribute, OrderDirection.ASC));
        return this;
    }

    @Override
    public <X> QueryResult<T> orderDesc(SingularAttribute<T, X> attribute) {
        context.addQueryStringPostProcessor(new OrderByQueryStringPostProcessor(attribute, OrderDirection.DESC));
        return this;
    }
    
    @Override
    public QueryResult<T> orderDesc(String attribute) {
        context.addQueryStringPostProcessor(new OrderByQueryStringPostProcessor(attribute, OrderDirection.DESC));
        return this;
    }
    
    @Override
    public <X> QueryResult<T> changeOrder(final SingularAttribute<T, X> attribute) {
        changeOrder(new ChangeOrder() {
            @Override
            public boolean matches(OrderByQueryStringPostProcessor orderBy) {
                return orderBy.matches(attribute);
            }
            @Override
            public void addDefault() {
                orderAsc(attribute);
            }
        });
        return this;
    }
    
    @Override
    public QueryResult<T> changeOrder(final String attribute) {
        changeOrder(new ChangeOrder() {
            @Override
            public boolean matches(OrderByQueryStringPostProcessor orderBy) {
                return orderBy.matches(attribute);
            }
            @Override
            public void addDefault() {
                orderAsc(attribute);
            }
        });
        return this;
    }
    
    @Override
    public QueryResult<T> clearOrder() {
        for (Iterator<QueryStringPostProcessor> it = context.getQueryStringPostProcessors().iterator(); it.hasNext();) {
            if (it.next() instanceof OrderByQueryStringPostProcessor) {
                it.remove();
            }
        }
        return this;
    }
    
    @Override
    public QueryResult<T> maxResults(int max) {
        context.addJpaQueryPostProcessor(new MaxResultPostProcessor(max));
        pageSize = max;
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
        return ((Query) builder.execute(context)).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getSingleResult() {
        return (T) ((Query) builder.execute(context)).getSingleResult();
    }

    @Override
    public long count() {
        CountQueryPostProcessor counter = new CountQueryPostProcessor();
        context.addJpaQueryPostProcessor(counter);
        try {
            Long result = (Long) ((Query) builder.execute(context)).getSingleResult();
            return result.intValue();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            context.removeJpaQueryPostProcessor(counter);
        }
    }
    
    @Override
    public QueryResult<T> withPageSize(int pageSize) {
        return maxResults(pageSize);
    }

    @Override
    public QueryResult<T> toPage(int page) {
        this.page = page;
        return firstResult(pageSize * page);
    }

    @Override
    public QueryResult<T> nextPage() {
        page = page + 1;
        return firstResult(pageSize * page);
    }

    @Override
    public QueryResult<T> previousPage() {
        page = page > 0 ? page - 1 : page;
        return firstResult(pageSize * page);
    }

    @Override
    public int countPages() {
        return (int) Math.ceil((double) count() / pageSize);
    }
    
    @Override
    public int currentPage() {
        return page;
    }
    
    @Override
    public int pageSize() {
        return pageSize;
    }

    private <X> QueryResult<T> changeOrder(ChangeOrder changeOrder) {
        for (QueryStringPostProcessor processor : context.getQueryStringPostProcessors()) {
            if (processor instanceof OrderByQueryStringPostProcessor) {
                OrderByQueryStringPostProcessor orderBy = (OrderByQueryStringPostProcessor) processor;
                if (changeOrder.matches(orderBy)) {
                    orderBy.changeDirection();
                    return this;
                }
            }
        }
        changeOrder.addDefault();
        return this;
    }
    
    private static abstract class ChangeOrder {
        
        public abstract boolean matches(OrderByQueryStringPostProcessor orderBy);
        
        public abstract void addDefault();

    }

}
