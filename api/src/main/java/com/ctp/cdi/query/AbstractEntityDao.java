package com.ctp.cdi.query;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import com.ctp.cdi.query.criteria.CriteriaSupport;


/**
 * Base DAO class to be extended by concrete implementations.
 * @author thomashug
 *
 * @param <E>   Entity type.
 * @param <PK>  Primary key type.
 */
@Dao
public abstract class AbstractEntityDao<E, PK extends Serializable> 
        implements EntityDao<E, PK>, CriteriaSupport<E> {

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
    
}
