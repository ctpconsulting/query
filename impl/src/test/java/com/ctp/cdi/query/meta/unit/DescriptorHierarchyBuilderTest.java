package com.ctp.cdi.query.meta.unit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DescriptorHierarchyBuilderTest {

    private final List<EntityDescriptor> entities = new LinkedList<EntityDescriptor>();
    private final List<MappedSuperclassDescriptor> superClasses = new LinkedList<MappedSuperclassDescriptor>();
    
    @Before
    public void before() {
        entities.add(new EntityDescriptor("test", null, EntityLevel3.class.getName(), null, null));
        entities.add(new EntityDescriptor("test", null, EntityLevel5.class.getName(), null, null));
        
        superClasses.add(new MappedSuperclassDescriptor("test", null, MappedLevel1.class.getName(), null, "id"));
        superClasses.add(new MappedSuperclassDescriptor("test", null, MappedLevel4.class.getName(), null, null));
        superClasses.add(new MappedSuperclassDescriptor("test", null, MappedUnrelated.class.getName(), null, null));
        superClasses.add(new MappedSuperclassDescriptor("test", null, MappedLevel2.class.getName(), null, null));
    }
    
    @Test
    public void should_build_hierarchy() {
        // given
        DescriptorHierarchyBuilder builder = DescriptorHierarchyBuilder.newInstance(entities, superClasses);
        
        // when
        builder.buildHierarchy();
        
        // then
        assertEquals(entities.size(), 2);
        assertEquals(EntityLevel3.class, entities.get(0).getEntityClass());
        assertEquals(EntityLevel5.class, entities.get(1).getEntityClass());
        assertEquals(MappedLevel2.class, entities.get(0).getParent().getEntityClass());
        assertEquals(MappedLevel4.class, entities.get(1).getParent().getEntityClass());
        
        assertEquals(superClasses.size(), 4);
        assertNull(superClasses.get(0).getParent());
        assertEquals(EntityLevel3.class, superClasses.get(1).getParent().getEntityClass());
        assertNull(superClasses.get(2).getParent());
        assertEquals(MappedLevel1.class, superClasses.get(3).getParent().getEntityClass());
    }
    
    private static class MappedLevel1 {
        @SuppressWarnings("unused")
        private Long id;
    }
    
    private static class MappedLevel2 extends MappedLevel1 {}
    
    private static class EntityLevel3 extends MappedLevel2 {}
    
    private static class MappedLevel4 extends EntityLevel3 {}
    
    private static class EntityLevel5 extends MappedLevel4 {}
    
    private static class MappedUnrelated {}
}
