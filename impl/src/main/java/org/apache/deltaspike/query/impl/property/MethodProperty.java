package org.apache.deltaspike.query.impl.property;

import java.lang.reflect.Method;

public interface MethodProperty<V> extends Property<V> {

    @Override
    public Method getAnnotatedElement();

}
