package com.ctp.cdi.query;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Can be used as query result type, which will not execute the query immediately.
 * Allows some post processing like defining query ordering.
 * 
 * @author thomashug
 * @param <T> Entity type
 */
public interface QueryResult<T> {

    <X> QueryResult<T> orderAsc(SingularAttribute<T, X> order);
    
    <X> QueryResult<T> orderAsc(String order);
    
    <X> QueryResult<T> orderDesc(SingularAttribute<T, X> order);
    
    <X> QueryResult<T> orderDesc(String order);
    
    List<T> getResultList();
    
    T getSingleResult();

}