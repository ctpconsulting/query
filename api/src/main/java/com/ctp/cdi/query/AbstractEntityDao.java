package com.ctp.cdi.query;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.Criteria;
import com.ctp.cdi.query.criteria.QuerySelection;


/**
 * Base DAO class to be extended by concrete implementations. Required to extend when
 * using the criteria utility API.
 * @author thomashug
 *
 * @param <E>   Entity type.
 * @param <PK>  Primary key type.
 */
@Dao
public abstract class AbstractEntityDao<E, PK extends Serializable> 
        implements EntityDao<E, PK> {

    /**
     * Utility method to get hold of the entity manager for this DAO.
     * This method can be overridden and decorated with qualifiers. If done, the qualifiers
     * will be used to resolve a specific entity manager other than the default one.
     * 
     * @return          Entity manager instance.
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Utility method to create a criteria query.
     * @return          Criteria query
     */
    protected abstract CriteriaQuery<E> criteriaQuery();
    
    /**
     * Get the entity class this DAO is related to.
     * @return          DAO entity class.
     */
    protected abstract Class<E> entityClass();
    
    /**
     * Create a {@link Criteria} instance.
     * @return          Criteria instance related to the DAO entity class.
     */
    protected abstract Criteria<E, E> criteria();
    
    /**
     * Create a {@link Criteria} instance.
     * @param <T>       Type related to the current criteria class.
     * @param clazz     Class other than the current entity class.
     * @return          Criteria instance related to a join type of the current entity class.
     */
    protected abstract <T> Criteria<T, T> where(Class<T> clazz);
    
    /**
     * Create a {@link Criteria} instance with a join type.
     * @param <T>       Type related to the current criteria class.
     * @param clazz     Class other than the current entity class.
     * @param joinType  Join type to apply.
     * @return          Criteria instance related to a join type of the current entity class.
     */
    protected abstract <T> Criteria<T, T> where(Class<T> clazz, JoinType joinType);
    
    /**
     * Create a query selection for an Entity attribute.
     * @param attribute Attribute to show up in the result selection
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract <X> QuerySelection<E, X> attribute(SingularAttribute<E, X> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#abs(javax.persistence.criteria.Expression)} 
     * over an attribute.
     * @param attribute Attribute to use in the aggregate.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract <N extends Number> QuerySelection<E, N> abs(SingularAttribute<E, N> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#avg(javax.persistence.criteria.Expression)} 
     * over an attribute.
     * @param attribute Attribute to use in the aggregate.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract <N extends Number> QuerySelection<E, N> avg(SingularAttribute<E, N> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#count(javax.persistence.criteria.Expression)} 
     * over an attribute.
     * @param attribute Attribute to use in the aggregate.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract <N extends Number> QuerySelection<E, N> count(SingularAttribute<E, N> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#max(javax.persistence.criteria.Expression)} 
     * over an attribute.
     * @param attribute Attribute to use in the aggregate.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract <N extends Number> QuerySelection<E, N> max(SingularAttribute<E, N> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#min(javax.persistence.criteria.Expression)} 
     * over an attribute.
     * @param attribute Attribute to use in the aggregate.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract <N extends Number> QuerySelection<E, N> min(SingularAttribute<E, N> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#neg(javax.persistence.criteria.Expression)} 
     * over an attribute.
     * @param attribute Attribute to use in the aggregate.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract <N extends Number> QuerySelection<E, N> neg(SingularAttribute<E, N> attribute);

    /**
     * Create a query selection for the {@link CriteriaBuilder#sum(javax.persistence.criteria.Expression)} 
     * over an attribute.
     * @param attribute Attribute to use in the aggregate.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract <N extends Number> QuerySelection<E, N> sum(SingularAttribute<E, N> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#mod(javax.persistence.criteria.Expression, Integer)} 
     * for an attribute.
     * @param attribute Attribute to use in the aggregate.
     * @param modulo    Modulo what.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract QuerySelection<E, Integer> modulo(SingularAttribute<E, Integer> attribute, Integer modulo);

    /**
     * Create a query selection for the {@link CriteriaBuilder#upper(javax.persistence.criteria.Expression)} 
     * over a String attribute.
     * @param attribute Attribute to uppercase.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract QuerySelection<E, String> upper(SingularAttribute<E, String> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#lower(javax.persistence.criteria.Expression)} 
     * over a String attribute.
     * @param attribute Attribute to lowercase.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract QuerySelection<E, String> lower(SingularAttribute<E, String> attribute);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#substring(javax.persistence.criteria.Expression, int)} 
     * over a String attribute.
     * @param attribute Attribute to create a substring from.
     * @param from      Substring start.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract QuerySelection<E, String> substring(SingularAttribute<E, String> attribute, int from);
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#substring(javax.persistence.criteria.Expression, int, int)} 
     * over a String attribute.
     * @param attribute Attribute to create a substring from.
     * @param from      Substring start.
     * @param length    Substring length.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract QuerySelection<E, String> substring(SingularAttribute<E, String> attribute, int from, int length);

    /**
     * Create a query selection for the {@link CriteriaBuilder#currentDate()}.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract QuerySelection<E, Date> currDate();
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#currentTime()}.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract QuerySelection<E, Time> currTime();
    
    /**
     * Create a query selection for the {@link CriteriaBuilder#currentTimestamp()}.
     * @return          {@link QuerySelection} part of a {@link Criteria#select(Class, QuerySelection...)} call.
     */
    protected abstract QuerySelection<E, Timestamp> currTStamp();
}
