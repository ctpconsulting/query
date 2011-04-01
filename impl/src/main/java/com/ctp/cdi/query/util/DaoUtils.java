package com.ctp.cdi.query.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jboss.logging.Logger;

import com.ctp.cdi.query.handler.DaoMetaData;

public abstract class DaoUtils {
    
    private static final Logger log = Logger.getLogger(DaoUtils.class);
    
    /**
     * Extract the generic type information from the DAO class or interface definition.
     * @param daoClass          DAO class to analyze.
     * @return                  Meta data containing entity and primary key classes.
     */
    public static DaoMetaData extractEntityMetaData(Class<?> daoClass) {
        DaoMetaData fromType = extractFrom(daoClass.getGenericSuperclass());
        if (fromType != null)
            return fromType;
        Class<?>[] interfaces = daoClass.getInterfaces();
        for (Class<?> interFace : interfaces) {
            for (Type type : interFace.getGenericInterfaces()) {
                DaoMetaData fromGenericInterface = extractFrom(type);
                if (fromGenericInterface != null)
                    return fromGenericInterface;
            }
        }
        Class<?> superClass = daoClass.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            return extractEntityMetaData(superClass);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private static DaoMetaData extractFrom(Type type) {
        log.debugv("extractFrom: type = {0}", type);
        if (type  instanceof ParameterizedType) {
            Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
            DaoMetaData result = null;
            for (Type genericType : genericTypes) {
                if (genericType instanceof Class && EntityUtils.isEntityClass((Class<?>) genericType)) {
                    result = new DaoMetaData((Class<?>) genericType);
                    continue;
                }
                if (result != null && genericType instanceof Class) {
                    result.setPrimaryClass((Class<? extends Serializable>) genericType);
                    return result;
                }
            }
        }
        return null;
    }

}
