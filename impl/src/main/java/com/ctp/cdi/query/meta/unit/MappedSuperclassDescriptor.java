package com.ctp.cdi.query.meta.unit;

import static com.ctp.cdi.query.util.QueryUtils.isEmpty;

import java.io.Serializable;


class MappedSuperclassDescriptor extends PersistentClassDescriptor {
    
    private MappedSuperclassDescriptor parent;

    MappedSuperclassDescriptor(String name, String packageName, String className, String idClass, String id) {
        super(name, packageName, className, idClass, id);
    }

    public void insert(MappedSuperclassDescriptor descriptor) {
        if (parent != null) {
            if (parent.getEntityClass().isAssignableFrom(descriptor.getEntityClass())) {
                descriptor.insert(parent);
                parent = descriptor;
            } else {
                parent.insert(descriptor);
            }
        } else {
            parent = descriptor;
        }
    }
    
    @Override
    public Class<? extends Serializable> getIdClass() {
        if (idClass == null && parent != null) {
            return parent.getIdClass();
        }
        return super.getIdClass();
    }

    @Override
    public String getId() {
        if (isEmpty(id) && parent != null) {
            return parent.getId();
        }
        return super.getId();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MappedSuperclassDescriptor ")
                .append("[entityClass=").append(className(entityClass))
                .append(", name=").append(name)
                .append(", idClass=").append(className(idClass))
                .append(", id=").append(id)
                .append(", parent=").append(parent)
                .append("]");
        return builder.toString();
    }

    public MappedSuperclassDescriptor getParent() {
        return parent;
    }

}
