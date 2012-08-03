package com.ctp.cdi.query.handler;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import org.jboss.solder.logging.Logger;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.QueryBuilderFactory;
import com.ctp.cdi.query.meta.DaoComponent;
import com.ctp.cdi.query.meta.DaoComponents;
import com.ctp.cdi.query.meta.DaoMethod;
import com.ctp.cdi.query.meta.Initialized;

/**
 * Entry point for query processing.
 * 
 * @author thomashug
 */
public class QueryHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Logger log = Logger.getLogger(getClass());
    
    @Inject @Any
    private Instance<EntityManager> entityManager;
    
    @Inject
    private QueryBuilderFactory queryBuilder;
    
    @Inject @Initialized
    private DaoComponents components;
    
    @AroundInvoke
    public Object handle(InvocationContext context) {
        QueryInvocationContext ctx = null;
        try {
            Class<?> daoClass = extractFromProxy(context);
            DaoComponent dao = components.lookupComponent(daoClass);
            DaoMethod method = components.lookupMethod(daoClass, context.getMethod());
            ctx = new QueryInvocationContext(context, method, resolveEntityManager(dao));
            QueryBuilder builder = queryBuilder.build(method);
            return builder.execute(ctx);
        } catch (Exception e) {
            log.error("Query execution error", e);
            if (ctx != null) {
                throw new QueryInvocationException(e, ctx);
            }
            throw new QueryInvocationException(e, context);
        }
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
            if (!ProxyObject.class.equals(interFace)) {
                return interFace;
            }
        }
        return null;
    }
    
    private EntityManager resolveEntityManager(DaoComponent dao) {
        Annotation[] qualifiers = extractFromTarget(dao.getDaoClass());
        if (qualifiers == null || qualifiers.length == 0) {
            qualifiers = dao.getEntityManagerQualifiers(); 
        }
        if (qualifiers == null || qualifiers.length == 0) {
            return entityManager.get();
        }
        return entityManager.select(qualifiers).get();
    }

    private Annotation[] extractFromTarget(Class<?> target) {
        try {
            Method method = target.getDeclaredMethod("getEntityManager");
            return method.getAnnotations();
        } catch (Exception e) {
            return null;
        }
    }

}
