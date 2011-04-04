package com.ctp.cdi.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Dao annotation needs to be present in order to have the
 * interface or class to be processed by the CDI extension.
 * 
 * @author thomashug
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Dao {
    Class<?> value() default Object.class;
}
