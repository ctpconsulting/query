package org.apache.deltaspike.query.impl.meta.extractor;

import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.api.NonEntity;
import org.apache.deltaspike.query.impl.meta.DaoEntity;
import org.apache.deltaspike.query.impl.meta.verifier.EntityVerifier;
import org.apache.deltaspike.query.impl.meta.verifier.Verifier;
import org.apache.deltaspike.query.impl.util.EntityUtils;


public class AnnotationMetadataExtractor implements MetadataExtractor {

    private final Verifier<Class<?>> verifier;

    public AnnotationMetadataExtractor() {
        this.verifier = new EntityVerifier();
    }

    @Override
    public DaoEntity extract(Class<?> daoClass) {
        Dao dao = daoClass.getAnnotation(Dao.class);
        Class<?> daoEntity = dao.value();
        boolean isEntityClass = !NonEntity.class.equals(daoEntity) && verifier.verify(daoEntity);
        if (isEntityClass) {
            return new DaoEntity(daoEntity, EntityUtils.primaryKeyClass(daoEntity));
        }
        return null;
    }

}
