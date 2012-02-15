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
    public void shouldExtractEntityClassFromDaoAnnotation() {
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
    public void shouldThrowExcptionWhenAnnotationWithEntityClassNotPresent() {
        // given
        AnnotationMetadataExtractor extractor = new AnnotationMetadataExtractor();

        // when
        DaoEntity result = extractor.extract(NoEntityPresentDao.class);

        // then
        assertNull(result);
    }

    @Test
    public void shouldThrowExceptionWhenAnnotationWithNonEntityClass() {
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
