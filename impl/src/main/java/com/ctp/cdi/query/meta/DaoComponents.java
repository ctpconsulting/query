package com.ctp.cdi.query.meta;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;

import com.ctp.cdi.query.util.DaoUtils;

public class DaoComponents {
    
    private static final Logger log = Logger.getLogger(DaoComponents.class);

    private Map<Class<?>, DaoComponent> daos = new HashMap<Class<?>, DaoComponent>();
    
    public boolean add(Class<?> daoClass) {
        DaoEntity entityClass = DaoUtils.extractEntityMetaData(daoClass);
        if (entityClass != null) {
            Set<Class<?>> collected = collectClasses(daoClass);
            DaoComponent dao = new DaoComponent(daoClass, collected, entityClass);
            daos.put(daoClass, dao);
            return true;
        }
        return false;
    }
    
    public DaoMethod lookupMethod(Class<?> daoClass, Method method) {
        log.debugv("lookupMethod(): {0} for declaring class {1}", method.getName(), daoClass.getName());
        if (daos.containsKey(daoClass)) {
            DaoComponent component = daos.get(daoClass);
            return component.lookupMethod(method);
        }
        throw new RuntimeException("Unknown DAO class " + daoClass.getName() + " with method " + method.getName());
    }
    
    private Set<Class<?>> collectClasses(Class<?> daoClass) {
        Set<Class<?>> result = new HashSet<Class<?>>();
        Class<?> current = daoClass;
        while (!Object.class.equals(current) && current != null) {
            result.add(current);
            Class<?>[] interfaces = current.getInterfaces();
            if (interfaces != null)
                result.addAll(Arrays.asList(interfaces));
            current = current.getSuperclass();
        }
        log.debugv("collectClasses(): Found {0} for {1}", result, daoClass);
        return result;
    }

}
