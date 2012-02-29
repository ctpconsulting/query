package com.ctp.cdi.query.meta.result;

import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Query;

import com.ctp.cdi.query.Modifying;

public class QueryProcessorFactory {

    private final Method method;

    private QueryProcessorFactory(Method method) {
        this.method = method;
    }
    
    public static QueryProcessorFactory newInstance(Method method) {
        return new QueryProcessorFactory(method);
    }
    
    public QueryProcessor build() {
        if (returns(List.class)) {
            return new ListQueryProcessor();
        }
        if (returns(Query.class)) {
            return new NoopQueryProcessor();
        }
        if (isModifying()) {
            return new ExecuteUpdateQueryProcessor(returns(Void.TYPE));
        }
        return new SingleResultQueryProcessor();
    }
    
    private boolean isModifying() {
        boolean matchesType = Void.TYPE.equals(method.getReturnType()) ||
                int.class.equals(method.getReturnType()) ||
                Integer.class.equals(method.getReturnType());
        return method.isAnnotationPresent(Modifying.class) && matchesType;
    }

    private boolean returns(Class<?> clazz) {
        return method.getReturnType().isAssignableFrom(clazz);
    }
    
    private static class ListQueryProcessor implements QueryProcessor {
        @Override
        public Object executeQuery(Query query) {
            return query.getResultList();
        }
    }
    
    private static class SingleResultQueryProcessor implements QueryProcessor {
        @Override
        public Object executeQuery(Query query) {
            return query.getSingleResult();
        }
    }
    
    private static class NoopQueryProcessor implements QueryProcessor {
        @Override
        public Object executeQuery(Query query) {
            return query;
        }
    }
    
    private static class ExecuteUpdateQueryProcessor implements QueryProcessor {
        
        private final boolean returnsVoid;

        private ExecuteUpdateQueryProcessor(boolean returnsVoid) {
            this.returnsVoid = returnsVoid;
        }
        
        @Override
        public Object executeQuery(Query query) {
            int result = query.executeUpdate();
            if (!returnsVoid) {
                return result;
            }
            return null;
        }
    }
}
