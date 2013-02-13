package com.ctp.cdi.query.property;

import java.lang.reflect.Field;

public interface FieldProperty<V> extends Property<V> {

    @Override
    public Field getAnnotatedElement();

}
