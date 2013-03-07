package org.apache.deltaspike.query.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Use this annotation when not using the default entity manager. If there are more than one,
 * CDI Query needs to know which one to use for a specific DAO.
 * 
 * @author thomashug
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Inherited
public @interface WithEntityManager {
    Class<? extends Annotation>[] value();
}
