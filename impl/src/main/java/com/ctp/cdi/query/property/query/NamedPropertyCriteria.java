package com.ctp.cdi.query.property.query;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
* A criteria that matches a property based on name
*
* @author Shane Bryzak
* @see PropertyCriteria
*/
public class NamedPropertyCriteria implements PropertyCriteria {
    private final String[] propertyNames;

    public NamedPropertyCriteria(String... propertyNames) {
        this.propertyNames = propertyNames;
    }

    @Override
    public boolean fieldMatches(Field f) {
        for (String propertyName : propertyNames) {
            if (propertyName.equals(f.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean methodMatches(Method m) {
        for (String propertyName : propertyNames) {
            if (m.getName().startsWith("get") &&
                    Introspector.decapitalize(m.getName().substring(3)).equals(propertyName)) {
                return true;
            }

        }
        return false;
    }
}
