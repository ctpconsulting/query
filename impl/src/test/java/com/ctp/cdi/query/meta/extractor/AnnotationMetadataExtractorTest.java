package com.ctp.cdi.query.meta.extractor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.meta.DaoEntity;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.DaoInterface;

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
