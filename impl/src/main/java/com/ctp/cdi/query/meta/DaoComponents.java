package com.ctp.cdi.query.meta;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import com.ctp.cdi.query.util.DaoUtils;

/**
 * Convenience class to access DAO and DAO method meta data. Acts as 
 * repository for DAO related meta data.
 * 
 * @author thomashug
 */
public class DaoComponents {
    
    private static final Logger log = Logger.getLogger(DaoComponents.class);

    private Map<Class<?>, DaoComponent> daos = new HashMap<Class<?>, DaoComponent>();
    
    /**
     * Add a DAO class to the meta data repository.
     * @param daoClass      The dao class.
     * @return              {@code true} if DAO class has been added,
     *                      {@code false} otherwise.
     */
    public boolean add(Class<?> daoClass) {
        DaoEntity entityClass = DaoUtils.extractEntityMetaData(daoClass);
        if (entityClass != null) {
            DaoComponent dao = new DaoComponent(daoClass, entityClass);
            daos.put(daoClass, dao);
            return true;
        }
        return false;
    }
    
    /**
     * Repository access - lookup the DAO component meta data for a specific DAO class.
     * @param daoClass      The DAO class to lookup the method for
     * @return              A {@link DaoComponent} corresponding to the daoClass parameter.
     */
    public DaoComponent lookupComponent(Class<?> daoClass) {
        if (daos.containsKey(daoClass)) {
            DaoComponent component = daos.get(daoClass);
            return component;
        }
        throw new RuntimeException("Unknown DAO class " + daoClass.getName());
    }
    
    /**
     * Repository access - lookup method information for a specific DAO class.
     * @param daoClass      The DAO class to lookup the method for
     * @param method        The Method object to get DAO meta data for.
     * @return              A {@link DaoMethod} corresponding to the method parameter.
     */
    public DaoMethod lookupMethod(Class<?> daoClass, Method method) {
        log.debugv("lookupMethod(): {0} for declaring class {1}", method.getName(), daoClass.getName());
        return lookupComponent(daoClass).lookupMethod(method);
    }

}
