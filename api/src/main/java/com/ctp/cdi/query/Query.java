package com.ctp.cdi.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

/**
 * Supply query meta data to a method with this annotation.<br/>
 * Currently supports:
 * <ul><li>JPQL queries as part of the annotation value</li>
 * <li>Execute named queries referenced by the named value</li>
 * <li>Execute native SQL queries</li>
 * <li>Restrict the result size to a static value</li>
 * <li>Provide a lock mode</li></ul>
 * @author thomashug
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Query {
    
    /**
     * Defines the Query to execute. Can be left empty for method expression queries
     * or when referencing a {@link #named()} query.
     */
    String value() default "";
    
    /**
     * References a named query.
     */
    String named() default "";
    
    /**
     * Defines a native SQL query.
     */
    String sql() default "";
    
    /**
     * Limits the number of results the query returns.
     */
    int max() default 0;
    
    /**
     * Defines a lock mode for the query.
     */
    LockModeType lock() default LockModeType.NONE;

    /** 
     * (Optional) Query properties and hints.  May include vendor-specific query hints.
     */
    QueryHint[] hints() default {};

}
