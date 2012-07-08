package com.ctp.cdi.query.meta;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctp.cdi.query.meta.extractor.AnnotationMetadataExtractor;
import com.ctp.cdi.query.meta.extractor.MetadataExtractor;
import com.ctp.cdi.query.meta.extractor.TypeMetadataExtractor;

/**
 * Convenience class to access DAO and DAO method meta data. Acts as 
 * repository for DAO related meta data.
 * 
 * @author thomashug
 */
public class DaoComponents implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Class<?>, DaoComponent> daos = new HashMap<Class<?>, DaoComponent>();
    
    private final List<MetadataExtractor> extractors = Arrays.asList(new AnnotationMetadataExtractor(),
            new TypeMetadataExtractor());
    
    /**
     * Add a DAO class to the meta data repository.
     * @param daoClass      The dao class.
     * @return              {@code true} if DAO class has been added,
     *                      {@code false} otherwise.
     */
    public boolean add(Class<?> daoClass) {
        DaoEntity entityClass = extractEntityMetaData(daoClass);
        if (entityClass != null) {
            DaoComponent dao = new DaoComponent(daoClass, entityClass);
            daos.put(daoClass, dao);
            return true;
        }
        /*
         * validate first and then dispatch based on metadata.
         */
        return false;
    }

    /**
     * Repository access - lookup the DAO component meta data for a specific DAO class.
     * @param daoClass      The DAO class to lookup the method for
     * @return              A {@link DaoComponent} corresponding to the daoClass parameter.
     */
    public DaoComponent lookupComponent(Class<?> daoClass) {
        if (daos.containsKey(daoClass)) {
            return daos.get(daoClass);
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
        return lookupComponent(daoClass).lookupMethod(method);
    }
    
    private DaoEntity extractEntityMetaData(Class<?> daoClass) {
        for (MetadataExtractor extractor : extractors) {
            DaoEntity entity = extractor.extract(daoClass);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

}
