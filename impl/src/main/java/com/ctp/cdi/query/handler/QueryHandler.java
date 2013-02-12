package com.ctp.cdi.query.handler;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.solder.logging.Logger;

import com.ctp.cdi.query.Dao;
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
@Dao
@RequestScoped
public class QueryHandler implements Serializable, InvocationHandler {

    private static final long serialVersionUID = 1L;

    private final Logger log = Logger.getLogger(getClass());

    @Inject @Any
    private Instance<EntityManager> entityManager;

    @Inject
    private QueryBuilderFactory queryBuilder;

    @Inject @Initialized
    private DaoComponents components;

    @Inject
    private Event<CdiQueryInvocationContext> contextCreated;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CdiQueryInvocationContext queryContext = null;
        try {
            Class<?> daoClass = extractFromProxy(proxy.getClass());
            DaoComponent dao = components.lookupComponent(daoClass);
            DaoMethod daoMethod = components.lookupMethod(daoClass, method);
            queryContext = createContext(proxy, method, args, dao, daoMethod);
            QueryBuilder builder = queryBuilder.build(daoMethod);
            return builder.execute(queryContext);
        } catch (Exception e) {
            log.error("Query execution error", e);
            if (queryContext != null) {
                throw new QueryInvocationException(e, queryContext);
            }
            throw new QueryInvocationException(e, proxy.getClass(), method);
        }
    }

    private CdiQueryInvocationContext createContext(Object proxy, Method method, Object[] args, DaoComponent dao, DaoMethod daoMethod) {
        CdiQueryInvocationContext queryContext = new CdiQueryInvocationContext(proxy, method, args, daoMethod, resolveEntityManager(dao));
        contextCreated.fire(queryContext);
        return queryContext;
    }

    protected Class<?> extractFromProxy(Class<?> proxyClass) {
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
