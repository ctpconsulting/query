package com.ctp.cdi.query.meta.unit;

import java.util.List;

public final class DescriptorHierarchyBuilder {

    private final List<EntityDescriptor> entities;
    private final List<MappedSuperclassDescriptor> superClasses;

    private DescriptorHierarchyBuilder(List<EntityDescriptor> entities,
            List<MappedSuperclassDescriptor> superClasses) {
        this.entities = entities;
        this.superClasses = superClasses;
    }
    
    public static DescriptorHierarchyBuilder newInstance(List<EntityDescriptor> entities,
            List<MappedSuperclassDescriptor> superClasses) {
        return new DescriptorHierarchyBuilder(entities, superClasses);
    }
    
    public void buildHierarchy() {
        for (EntityDescriptor descriptor : entities) {
            buildHierarchy(descriptor);
        }
    }

    private void buildHierarchy(PersistentClassDescriptor descriptor) {
        Class<?> superClass = descriptor.getEntityClass().getSuperclass();
        while (superClass != null) {
            PersistentClassDescriptor superDescriptor = findPersistentClassDescriptor(superClass);
            if (superDescriptor != null) {
                if (descriptor.getParent() == null) {
                    buildHierarchy(superDescriptor);
                }
                descriptor.setParent(superDescriptor);
                return;
            }
            superClass = superClass.getSuperclass();
        }
    }

    private PersistentClassDescriptor findPersistentClassDescriptor(Class<?> superClass) {
        for (MappedSuperclassDescriptor descriptor : superClasses) {
            if (descriptor.getEntityClass().equals(superClass)){
                return descriptor;
            }
        }
        for (EntityDescriptor descriptor : entities) {
            if (descriptor.getEntityClass().equals(superClass)){
                return descriptor;
            }
        }
        return null;
    }




    
}
