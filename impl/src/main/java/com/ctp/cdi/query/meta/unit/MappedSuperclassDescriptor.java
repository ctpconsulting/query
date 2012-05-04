package com.ctp.cdi.query.meta.unit;

import static com.ctp.cdi.query.util.QueryUtils.isEmpty;

import java.io.Serializable;


class MappedSuperclassDescriptor extends PersistentClassDescriptor {
    
    

    MappedSuperclassDescriptor(String name, String packageName, String className, String idClass, String id) {
        super(name, packageName, className, idClass, id);
    }

    
    @Override
    public Class<? extends Serializable> getIdClass() {
        if (idClass == null && getParent() != null) {
            return getParent().getIdClass();
        }
        return super.getIdClass();
    }

    @Override
    public String getId() {
        if (isEmpty(id) && getParent() != null) {
            return getParent().getId();
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
                .append(", parent=").append(getParent())
                .append("]");
        return builder.toString();
    }



}
