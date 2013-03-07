package org.apache.deltaspike.query.api.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a property which should be updated with a timestamp when the entity gets updated.
 * By settings {@link #onCreate()} to {@code true}, the property gets also set when 
 * the entity is persisted.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ModifiedOn {
    boolean onCreate() default false;
}