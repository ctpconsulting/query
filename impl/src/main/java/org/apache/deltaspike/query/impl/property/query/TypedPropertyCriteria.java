package org.apache.deltaspike.query.impl.property.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
* A criteria that matches a property based on its type
*
* @author Shane Bryzak
* @see PropertyCriteria
*/
public class TypedPropertyCriteria implements PropertyCriteria {
    private final Class<?> propertyClass;

    public TypedPropertyCriteria(Class<?> propertyClass) {
        this.propertyClass = propertyClass;
    }

    @Override
    public boolean fieldMatches(Field f) {
        return propertyClass.equals(f.getType());
    }

    @Override
    public boolean methodMatches(Method m) {
        return propertyClass.equals(m.getReturnType());
    }
}
