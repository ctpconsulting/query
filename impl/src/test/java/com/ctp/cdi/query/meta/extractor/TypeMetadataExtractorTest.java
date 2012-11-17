package com.ctp.cdi.query.meta.extractor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

import com.ctp.cdi.query.meta.DaoEntity;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.DaoInterface;
import com.ctp.cdi.query.test.service.SimpleDao;

public class TypeMetadataExtractorTest {

    @Test
    public void should_extract_from_class() {
        // given
        MetadataExtractor extractor = new TypeMetadataExtractor();
        
        // when
        DaoEntity result = extractor.extract(SimpleDao.class);
        
        // then
        assertNotNull(result);
        assertEquals(Simple.class, result.getEntityClass());
        assertEquals(Long.class, result.getPrimaryClass());
    }
    
    @Test
    public void should_not_extract_from_annotation() {
        // given
        MetadataExtractor extractor = new TypeMetadataExtractor();
        
        // when
        DaoEntity result = extractor.extract(DaoInterface.class);
        
        // then
        assertNull(result);
    }
    
}
