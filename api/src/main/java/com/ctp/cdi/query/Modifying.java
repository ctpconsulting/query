package com.ctp.cdi.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.Query;

/**
 * Marks a query method to be modifying. This will execute the {@link Query#executeUpdate()}
 * method instead of {@link Query#getResultList()} (or the corresponding single result method).
 * 
 * @author thomashug
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Modifying {
}
