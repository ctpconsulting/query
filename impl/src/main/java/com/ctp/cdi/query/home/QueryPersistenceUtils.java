package com.ctp.cdi.query.home;

import com.ctp.cdi.query.meta.extractor.TypeMetadataExtractor;
import com.ctp.cdi.query.util.EntityUtils;

public class QueryPersistenceUtils<E, PK> implements PersistenceUtils<E, PK> {

    private static final long serialVersionUID = 1L;

    @Override
    @SuppressWarnings("unchecked")
    public Class<E> entityClass(Class<?> homeClass) {
        return (Class<E>) new TypeMetadataExtractor().extract(homeClass).getEntityClass();
    }

    @Override
    @SuppressWarnings("unchecked")
    public PK primaryKeyValue(Object entity) {
        return (PK) EntityUtils.primaryKeyValue(entity);
    }

}
