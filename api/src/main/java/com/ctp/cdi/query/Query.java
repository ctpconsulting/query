package com.ctp.cdi.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.LockModeType;

/**
 * Supply query meta data to a method with this annotation.<br/>
 * Currently supports:
 * <ul><li>JPQL queries as part of the annotation value</li>
 * <li>Execute named queries referenced by the named value</li>
 * <li>Restrict the result size to a static value</li>
 * <li>Provide a lock mode</li></ul>
 * @author thomashug
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Query {
    String value() default "";
    String named() default "";
    int max() default 0;
    LockModeType lock() default LockModeType.NONE;
}
