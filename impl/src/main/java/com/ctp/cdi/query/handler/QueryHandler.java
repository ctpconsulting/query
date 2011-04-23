package com.ctp.cdi.query.handler;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.meta.DaoComponents;
import com.ctp.cdi.query.meta.DaoMethod;
import com.ctp.cdi.query.meta.Initialized;
import com.ctp.cdi.query.meta.QueryInvocationLiteral;

/**
 * Entry point for query processing.
 * 
 * @author thomashug
 */
public class QueryHandler {
    
    @Inject @Any
    private Instance<EntityManager> entityManager;
    
    @Inject @Any
    private Instance<QueryBuilder> queryBuilder;
    
    @Inject @Initialized
    private DaoComponents components;
    
    @AroundInvoke
    public Object handle(InvocationContext context) throws Exception {
        Class<?> daoClass = extractFromProxy(context);
        DaoMethod method = components.lookupMethod(daoClass, context.getMethod());
        QueryBuilder builder = queryBuilder.select(new QueryInvocationLiteral(method.getMethodType())).get();
        return builder.execute(new QueryInvocationContext(context, method, entityManager.get()));
    }
    
    protected Class<?> extractFromProxy(InvocationContext ctx) {
        Class<?> proxyClass = ctx.getTarget().getClass();
        if (ProxyFactory.isProxyClass(proxyClass)) {
            if (isInterfaceProxy(proxyClass)) {
                return extractFromInterface(proxyClass);
            } else {
                return proxyClass.getSuperclass();
            }
        }
        return proxyClass;
    }
    
    private boolean isInterfaceProxy(Class<?> proxyClass) {
        Class<?>[] interfaces = proxyClass.getInterfaces();
        return Object.class.equals(proxyClass.getSuperclass()) && 
                interfaces != null && interfaces.length > 0;
    }
    
    private Class<?> extractFromInterface(Class<?> proxyClass) {
        for (Class<?> interFace : proxyClass.getInterfaces()) {
            if (!ProxyObject.class.equals(interFace))
                return interFace;
        }
        return null;
    }

}
