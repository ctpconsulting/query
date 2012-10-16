package com.ctp.cdi.query.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;

import com.ctp.cdi.query.handler.CdiQueryInvocationContext;
import com.ctp.cdi.query.handler.QueryInvocationException;
import com.ctp.cdi.query.meta.MethodType;
import com.ctp.cdi.query.meta.QueryInvocation;
import com.ctp.cdi.query.spi.DelegateQueryHandler;

@QueryInvocation(MethodType.DELEGATE)
public class DelegateQueryBuilder extends QueryBuilder {
    
    @Inject @Any
    private Instance<DelegateQueryHandler> delegates;

    @Override
    public Object execute(CdiQueryInvocationContext context) {
        try {
            InvocationContext invocation = context.getInvocation();
            DelegateQueryHandler delegate = selectDelegate(context.getMethod());
            if (delegate != null) {
                return invoke(delegate, context);
            }
            return invocation.proceed();
        } catch (Exception e) {
            throw new QueryInvocationException(e, context);
        }
    }
    
    private DelegateQueryHandler selectDelegate(Method method) {
        for (Iterator<DelegateQueryHandler> it = delegates.iterator(); it.hasNext();) {
            DelegateQueryHandler delegate = it.next();
            if (contains(delegate, method)) {
                return delegate;
            }
        }
        return null;
    }
    
    private boolean contains(Object obj, Method method) {
        return extract(obj, method) != null;
    }
    
    private Method extract(Object obj, Method method) {
        try {
            String name = method.getName();
            return obj.getClass().getMethod(name, method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    private Object invoke(DelegateQueryHandler delegate, CdiQueryInvocationContext context) {
        InvocationContext invocation = context.getInvocation();
        try {
            return invoke(delegate, invocation.getMethod(), invocation.getParameters());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected Object invoke(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Method extract = extract(target, method);
        return extract.invoke(target, args);
    }

}
