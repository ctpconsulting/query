package com.ctp.cdi.query;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.AttributeQuerySelection;
import com.ctp.cdi.query.criteria.Criteria;
import com.ctp.cdi.query.criteria.AggregateQuerySelection;
import com.ctp.cdi.query.criteria.AggregateQuerySelection.Operator;
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
public abstract class AbstractEntityDao<E, PK extends Serializable> implements EntityDao<E, PK> {

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
    protected Criteria<E, E> criteria() {
        return new Criteria<E, E>(entityClass(), entityClass(), getEntityManager());
    }
    
    /**
     * Create a {@link Criteria} instance.
     * @param <T>       Type related to the current criteria class.
     * @param clazz     Class other than the current entity class.
     * @return          Criteria instance related to a join type of the current entity class.
     */
    protected <T> Criteria<T, T> where(Class<T> clazz) {
        return new Criteria<T, T>(clazz, clazz, getEntityManager());
    }
    
    /**
     * Create a {@link Criteria} instance with a join type.
     * @param <T>       Type related to the current criteria class.
     * @param clazz     Class other than the current entity class.
     * @param joinType  Join type to apply.
     * @return          Criteria instance related to a join type of the current entity class.
     */
    protected <T> Criteria<T, T> where(Class<T> clazz, JoinType joinType) {
        return new Criteria<T, T>(clazz, clazz, getEntityManager(), joinType);
    }
    
    protected <X> QuerySelection<E, X> att(SingularAttribute<E, X> attribute) {
        return new AttributeQuerySelection<E, X>(attribute);
    }
    
    protected <N extends Number> QuerySelection<E, N> abs(SingularAttribute<E, N> attribute) {
        return new AggregateQuerySelection<E, N>(Operator.ABS, attribute);
    }
    
    protected <N extends Number> QuerySelection<E, N> avg(SingularAttribute<E, N> attribute) {
        return new AggregateQuerySelection<E, N>(Operator.AVG, attribute);
    }
    
    protected <N extends Number> QuerySelection<E, N> count(SingularAttribute<E, N> attribute) {
        return new AggregateQuerySelection<E, N>(Operator.COUNT, attribute);
    }

}
