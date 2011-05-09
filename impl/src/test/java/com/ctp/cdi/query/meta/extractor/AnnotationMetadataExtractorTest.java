package com.ctp.cdi.query.meta.extractor;

import junit.framework.Assert;

import org.junit.Test;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.meta.DaoEntity;
import com.ctp.cdi.query.meta.NonEntityException;
import com.ctp.cdi.query.meta.extractor.AnnotationMetadataExtractor;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.DaoInterface;

public class AnnotationMetadataExtractorTest {

	@Test
	public void shouldExtractEntityClassFromDaoAnnotation() {
		// given
		AnnotationMetadataExtractor extractor = new AnnotationMetadataExtractor(
				DaoInterface.class);

		// when
		DaoEntity result = extractor.extract();

		// then
		Assert.assertNotNull(result);
		Assert.assertEquals(Simple.class, result.getEntityClass());
		Assert.assertEquals(Long.class, result.getPrimaryClass());
	}

	@Test(expected = NonEntityException.class)
	public void shouldThrowExcptionWhenAnnotationWithEntityClassNotPresent() {
		// given
		AnnotationMetadataExtractor extractor = new AnnotationMetadataExtractor(
				NoEntityPresentDao.class);

		// when
		extractor.extract();
		
		// then Exception should be thrown
	}

	@Test(expected = NonEntityException.class)
	public void shouldThrowExceptionWhenAnnotationWithNonEntityClass() {
		// given
		AnnotationMetadataExtractor extractor = new AnnotationMetadataExtractor(
				NonEntityDao.class);

		// when
		extractor.extract();
		
		// then Exception should be thrown
	}

	@Dao
	private static class NoEntityPresentDao {}
	
	@Dao(Object.class)
	private static class NonEntityDao {}
}
