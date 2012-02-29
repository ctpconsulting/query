package com.ctp.cdi.query.meta.unit;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DescriptorHierarchyBuilder {

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
        Set<MappedSuperclassDescriptor> filtered = buildSuperclassHierarchy();
        for (EntityDescriptor descriptor : entities) {
            assign(descriptor, filtered);
        }
    }

    private void assign(EntityDescriptor entity, Set<MappedSuperclassDescriptor> filtered) {
        for (MappedSuperclassDescriptor superClass : filtered) {
            if (entity.assignSuperclassIfMatching(superClass)) {
                return;
            }
        }
    }

    private Set<MappedSuperclassDescriptor> buildSuperclassHierarchy() {
        Set<MappedSuperclassDescriptor> result = new HashSet<MappedSuperclassDescriptor>();
        for (MappedSuperclassDescriptor descriptor : superClasses) {
            addToSet(descriptor, result);
        }
        return result;
    }

    private void addToSet(MappedSuperclassDescriptor descriptor, Set<MappedSuperclassDescriptor> result) {
        for (Iterator<MappedSuperclassDescriptor> it = result.iterator(); it.hasNext();) {
            MappedSuperclassDescriptor current = it.next();
            if (descriptor.getEntityClass().isAssignableFrom(current.getEntityClass())) {
                current.insert(descriptor);
                return;
            }
            if (current.getEntityClass().isAssignableFrom(descriptor.getEntityClass())) {
                descriptor.insert(current);
                it.remove();
            }
        }
        result.add(descriptor);
    }
    
}
