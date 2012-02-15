package com.ctp.cdi.query.meta.extractor;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jboss.solder.logging.Logger;

import com.ctp.cdi.query.meta.DaoEntity;
import com.ctp.cdi.query.util.EntityUtils;

public class TypeMetadataExtractor implements MetadataExtractor {
    
    private final Logger log = Logger.getLogger(getClass());

    @Override
    public DaoEntity extract(Class<?> daoClass) {
        for (Type inf : daoClass.getGenericInterfaces()) {
            DaoEntity result = extractFrom(inf);
            if (result != null) {
                return result;
            }
        }
        DaoEntity result = extractFrom(daoClass.getGenericSuperclass());
        if (result != null) {
            return result;
        }
        for (Type intf : daoClass.getGenericInterfaces()) {
            result = extractFrom(intf);
            if (result != null) {
                return result;
            }
        }
        if (daoClass.getSuperclass() != null) {
            return extract(daoClass.getSuperclass());
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private DaoEntity extractFrom(Type type) {
        log.debugv("extractFrom: type = {0}", type);
        if (!(type  instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parametrizedType = (ParameterizedType) type;
        Type[] genericTypes = parametrizedType.getActualTypeArguments();
        DaoEntity result = null;
        for (Type genericType : genericTypes) {
            if (genericType instanceof Class && EntityUtils.isEntityClass((Class<?>) genericType)) {
                result = new DaoEntity((Class<?>) genericType);
                continue;
            }
            if (result != null && genericType instanceof Class) {
                result.setPrimaryClass((Class<? extends Serializable>) genericType);
                return result;
            }
        }
        return result;
    }

}
