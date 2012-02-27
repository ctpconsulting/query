package com.ctp.cdi.query.meta.unit;

import static com.ctp.cdi.query.util.QueryUtils.isEmpty;

import java.io.Serializable;


class EntityDescriptor extends PersistentClassDescriptor {
    
    private MappedSuperclassDescriptor superClass;

    EntityDescriptor(String name, String packageName, String className, String idClass, String id) {
        super(name, packageName, className, idClass, id);
    }
    
    public boolean is(Class<?> entityClass) {
        return this.entityClass.equals(entityClass);
    }
    
    @Override
    public Class<? extends Serializable> getIdClass() {
        if (idClass == null && superClass != null) {
            return superClass.getIdClass();
        }
        return super.getIdClass();
    }

    @Override
    public String getId() {
        if (isEmpty(id) && superClass != null) {
            return superClass.getId();
        }
        return super.getId();
    }

    public boolean assignSuperclassIfMatching(MappedSuperclassDescriptor superClass) {
        MappedSuperclassDescriptor current = superClass;
        while (current != null) {
            if (current.getEntityClass().isAssignableFrom(entityClass)) {
                this.superClass = current;
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityDescriptor ")
                .append("[entityClass=").append(className(entityClass))
                .append(", name=").append(name)
                .append(", idClass=").append(className(idClass))
                .append(", id=").append(id)
                .append(", superClass=").append(superClass)
                .append("]");
        return builder.toString();
    }

    public MappedSuperclassDescriptor getSuperClass() {
        return superClass;
    }

    public void setSuperClass(MappedSuperclassDescriptor superClass) {
        this.superClass = superClass;
    }
    
}
