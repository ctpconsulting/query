package org.apache.deltaspike.query.impl.meta.extractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.impl.meta.DaoEntity;
import org.apache.deltaspike.query.impl.meta.extractor.AnnotationMetadataExtractor;
import org.apache.deltaspike.query.test.domain.Simple;
import org.apache.deltaspike.query.test.service.DaoInterface;
import org.junit.Test;


public class AnnotationMetadataExtractorTest {

    @Test
    public void should_extract_entity_class_from_dao_annotation() {
        // given
        AnnotationMetadataExtractor extractor = new AnnotationMetadataExtractor();

        // when
        DaoEntity result = extractor.extract(DaoInterface.class);

        // then
        assertNotNull(result);
        assertEquals(Simple.class, result.getEntityClass());
        assertEquals(Long.class, result.getPrimaryClass());
    }

    @Test
    public void should_throw_excption_when_annotation_with_entity_class_not_present() {
        // given
        AnnotationMetadataExtractor extractor = new AnnotationMetadataExtractor();

        // when
        DaoEntity result = extractor.extract(NoEntityPresentDao.class);

        // then
        assertNull(result);
    }

    @Test
    public void should_throw_exception_when_annotation_with_non_entity_class() {
        // given
        AnnotationMetadataExtractor extractor = new AnnotationMetadataExtractor();

        // when
        DaoEntity result = extractor.extract(NonEntityDao.class);

        // then
        assertNull(result);
    }

    @Dao
    private static class NoEntityPresentDao {
    }

    @Dao(Object.class)
    private static class NonEntityDao {
    }
}
