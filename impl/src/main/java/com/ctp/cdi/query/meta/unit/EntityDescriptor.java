package com.ctp.cdi.query.meta.unit;


class EntityDescriptor extends PersistentClassDescriptor {

    EntityDescriptor(String name, String packageName, String className, String idClass, String id) {
        super(name, packageName, className, idClass, id);
    }
    
    public boolean is(Class<?> entityClass) {
        return this.entityClass.equals(entityClass);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EntityMapping [name=").append(name)
               .append(", entityClass=").append(entityClass)
               .append(", idClass=").append(idClass)
               .append(", id=").append(id).append("]");
        return builder.toString();
    }
    
}
