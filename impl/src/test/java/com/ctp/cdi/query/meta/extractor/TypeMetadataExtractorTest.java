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
    public void shouldExtractFromClass() {
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
    public void shouldNotExtractFromAnnotation() {
        // given
        MetadataExtractor extractor = new TypeMetadataExtractor();
        
        // when
        DaoEntity result = extractor.extract(DaoInterface.class);
        
        // then
        assertNull(result);
    }
    
}
