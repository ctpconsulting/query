package com.ctp.cdi.query;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Can be used as query result type, which will not execute the query immediately.
 * Allows some post processing like defining query ordering.
 * 
 * @author thomashug
 * @param <E> Entity type
 */
public interface QueryResult<E> {

    /**
     * Sort the query result ascending by the given entity singular attribute.
     * This is the typesafe version, alternatively a {@link #orderAsc(String)} 
     * String can be used.
     * 
     * Note that this is not applicable to named queries.
     * 
     * @param order             Sort attribute.
     * @return                  Fluent API: the result instance.
     */
    <X> QueryResult<E> orderAsc(SingularAttribute<E, X> order);
    
    /**
     * Sort the query result ascending by the given entity attribute.
     * 
     * Note that this is not applicable to named queries.
     * 
     * @param order             Sort attribute.
     * @return                  Fluent API: the result instance.
     */
    QueryResult<E> orderAsc(String order);
    
    /**
     * Sort the query result descending by the given entity singular attribute.
     * This is the typesafe version, alternatively a {@link #orderDesc(String)} 
     * String can be used.
     * 
     * Note that this is not applicable to named queries.
     * 
     * @param order             Sort attribute.
     * @return                  Fluent API: the result instance.
     */
    <X> QueryResult<E> orderDesc(SingularAttribute<E, X> order);
    
    /**
     * Sort the query result descending by the given entity attribute.
     * 
     * Note that this is not applicable to named queries.
     * 
     * @param order             Sort attribute.
     * @return                  Fluent API: the result instance.
     */
    QueryResult<E> orderDesc(String order);
    
    /**
     * Limit the number of results returned by the query.
     * @param max               Max number of results.
     * @return                  Fluent API: the result instance.
     */
    QueryResult<E> maxResults(int max);
    
    /**
     * Pagination: Set the result start position.
     * @param first             Result start position.
     * @return                  Fluent API: the result instance.
     */
    QueryResult<E> firstResult(int first);
    
    /**
     * Sets the query lock mode.
     * @param lockMode          Query lock mode to use in the query.
     * @return                  Fluent API: the result instance.
     */
    QueryResult<E> lockMode(LockModeType lockMode);
    
    /**
     * Sets the query flush mode.
     * @param flushMode         Query flush mode to use in the query.
     * @return                  Fluent API: the result instance.
     */
    QueryResult<E> flushMode(FlushModeType flushMode);
    
    /**
     * Apply a query hint to the query to execute.
     * @param hint              Hint name.
     * @param value             Hint value.
     * @return                  Fluent API: the result instance.
     */
    QueryResult<E> hint(String hint, Object value);
    
    /**
     * Fetch the result set.
     * @return                  List of entities retrieved by the query.
     */
    List<E> getResultList();
    
    /**
     * Fetch a single result entity.
     * @return                  Entity retrieved by the query.
     */
    E getSingleResult();
    
    /**
     * Count the result set.
     * @return                  Result count.
     */
    long count();

}