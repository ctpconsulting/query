package com.ctp.cdi.query.meta.extractor;

import com.ctp.cdi.query.meta.DaoEntity;

public class TypeMetadataExtractor implements MetadataExtractor {

    private final Class<?> dao;

    public TypeMetadataExtractor(Class<?> dao) {
        this.dao = dao;
    }

    @Override
    public DaoEntity extract() {
        return null;
    }

}
