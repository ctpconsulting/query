package com.ctp.cdi.query.home;

import com.ctp.cdi.query.meta.extractor.TypeMetadataExtractor;
import com.ctp.cdi.query.util.EntityUtils;

public class QueryPersistenceUtils implements PersistenceUtils {

    private static final long serialVersionUID = 1L;

    @Override
    public Class<?> entityClass(Class<?> homeClass) {
        return new TypeMetadataExtractor().extract(homeClass).getEntityClass();
    }

    @Override
    public Object primaryKeyValue(Object entity) {
        return EntityUtils.primaryKeyValue(entity);
    }

}
