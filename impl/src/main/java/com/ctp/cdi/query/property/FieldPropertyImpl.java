package com.ctp.cdi.query.property;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

/**
* A bean property based on the value contained in a field
*
* @author Pete Muir
* @author Shane Bryzak
*/
class FieldPropertyImpl<V> implements FieldProperty<V> {

    private final Field field;

    FieldPropertyImpl(Field field) {
        this.field = field;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public Type getBaseType() {
        return field.getGenericType();
    }

    @Override
    public Field getAnnotatedElement() {
        return field;
    }

    @Override
    public Member getMember() {
        return field;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<V> getJavaClass() {
        return (Class<V>) field.getType();
    }

    @Override
    public V getValue(Object instance) {
        setAccessible();
        return Reflections.getFieldValue(field, instance, getJavaClass());
    }

    @Override
    public void setValue(Object instance, V value) {
        setAccessible();
        Reflections.setFieldValue(true, field, instance, value);
    }

    @Override
    public Class<?> getDeclaringClass() {
        return field.getDeclaringClass();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setAccessible() {
        Reflections.setAccessible(field);
    }

    @Override
    public String toString() {
        return field.toString();
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return field.equals(obj);
    }
}
