package com.ctp.cdi.query.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

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
            DelegateQueryHandler delegate = selectDelegate(context.getMethod());
            if (delegate != null) {
                return invoke(delegate, context);
            }
        } catch (Exception e) {
            throw new QueryInvocationException(e, context);
        }
        throw new QueryInvocationException("No DelegateQueryHandler found", context);
    }

    private DelegateQueryHandler selectDelegate(Method method) {
        for (DelegateQueryHandler delegate : delegates) {
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
            return obj != null ? obj.getClass().getMethod(name, method.getParameterTypes()) : null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Object invoke(DelegateQueryHandler delegate, CdiQueryInvocationContext context) {
        try {
            return invoke(delegate, context.getMethod(), context.getMethodParameters());
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
