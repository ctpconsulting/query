package com.ctp.cdi.query.property.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
* A criteria that matches a property based on its annotations
*
* @author Shane Bryzak
* @see PropertyCriteria
*/
public class AnnotatedPropertyCriteria implements PropertyCriteria {
    private final Class<? extends Annotation> annotationClass;

    public AnnotatedPropertyCriteria(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public boolean fieldMatches(Field f) {
        return f.isAnnotationPresent(annotationClass);
    }

    @Override
    public boolean methodMatches(Method m) {
        return m.isAnnotationPresent(annotationClass);
    }

}
