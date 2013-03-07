package com.ctp.cdi.query.handler;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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
@ApplicationScoped
public class QueryHandler implements Serializable, InvocationHandler {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(QueryHandler.class.getName());

    @Inject @Any
    private Instance<EntityManager> entityManager;

    @Inject
    private QueryBuilderFactory queryBuilder;

    @Inject @Initialized
    private DaoComponents components;

    @Inject
    private CdiQueryContextHolder context;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CdiQueryInvocationContext queryContext = null;
        try {
            List<Class<?>> candidates = extractFromProxy(proxy.getClass());
            DaoComponent dao = components.lookupComponent(candidates);
            DaoMethod daoMethod = components.lookupMethod(dao.getDaoClass(), method);
            queryContext = createContext(proxy, method, args, dao, daoMethod);
            QueryBuilder builder = queryBuilder.build(daoMethod);
            Object result = builder.execute(queryContext);
            return result;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Query execution error", e);
            if (queryContext != null) {
                throw new QueryInvocationException(e, queryContext);
            }
            throw new QueryInvocationException(e, proxy.getClass(), method);
        } finally {
            context.dispose();
        }
    }

    private CdiQueryInvocationContext createContext(Object proxy, Method method, Object[] args, DaoComponent dao, DaoMethod daoMethod) {
        CdiQueryInvocationContext queryContext = new CdiQueryInvocationContext(proxy, method, args, daoMethod, resolveEntityManager(dao));
        context.set(queryContext);
        return queryContext;
    }

    protected List<Class<?>> extractFromProxy(Class<?> proxyClass) {
        List<Class<?>> result = new LinkedList<Class<?>>();
        result.add(proxyClass);
        if (isInterfaceProxy(proxyClass)) {
            result.addAll(Arrays.asList(proxyClass.getInterfaces()));
        } else {
            result.add(proxyClass.getSuperclass());
        }
        return result;
    }

    private boolean isInterfaceProxy(Class<?> proxyClass) {
        Class<?>[] interfaces = proxyClass.getInterfaces();
        return Object.class.equals(proxyClass.getSuperclass()) &&
                interfaces != null && interfaces.length > 0;
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
