package org.apache.deltaspike.query.impl.meta.unit;

import static org.apache.deltaspike.query.impl.util.QueryUtils.isEmpty;

import java.io.Serializable;


class EntityDescriptor extends PersistentClassDescriptor {

    EntityDescriptor(String name, String packageName, String className, String idClass, String id) {
        super(name, packageName, className, idClass, id);
    }
    
    public boolean is(Class<?> entityClass) {
        return this.entityClass.equals(entityClass);
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
        builder.append("EntityDescriptor ")
                .append("[entityClass=").append(className(entityClass))
                .append(", name=").append(name)
                .append(", idClass=").append(className(idClass))
                .append(", id=").append(id)
                .append(", superClass=").append(getParent())
                .append("]");
        return builder.toString();
    }
    
}
