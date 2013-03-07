package org.apache.deltaspike.query.impl.meta.extractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.deltaspike.query.impl.meta.DaoEntity;
import org.apache.deltaspike.query.impl.meta.extractor.MetadataExtractor;
import org.apache.deltaspike.query.impl.meta.extractor.TypeMetadataExtractor;
import org.apache.deltaspike.query.test.domain.Simple;
import org.apache.deltaspike.query.test.service.DaoInterface;
import org.apache.deltaspike.query.test.service.SimpleDao;
import org.junit.Test;


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
