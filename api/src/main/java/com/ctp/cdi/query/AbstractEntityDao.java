package com.ctp.cdi.query;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;


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
    protected Criteria<E> criteria() {
        return new Criteria<E>(entityClass());
    }
    
    /**
     * Create a {@link Criteria} instance.
     * @param <T>       Type related to the current criteria class.
     * @param clazz     Class other than the current entity class.
     * @return          Criteria instance related to a join type of the current entity class.
     */
    protected <T> Criteria<T> where(Class<T> clazz) {
        return new Criteria<T>(clazz);
    }
    
    /**
     * Create a {@link Criteria} instance with a join type.
     * @param <T>       Type related to the current criteria class.
     * @param clazz     Class other than the current entity class.
     * @param joinType  Join type to apply.
     * @return          Criteria instance related to a join type of the current entity class.
     */
    protected <T> Criteria<T> where(Class<T> clazz, JoinType joinType) {
        return new Criteria<T>(clazz, joinType);
    }
    
    private static enum PredicateType {
        AND, OR
    }
    
    /**
     * Utility class to create JPA Criteria API queries.
     * @author thomashug
     *
     * @param <C> The criteria is related to a specific type.
     */
    protected class Criteria<C> {

        private CriteriaBuilder builder;
        private CriteriaQuery<C> query;
        private Root<C> root;
        private JoinType joinType;
        
        private Map<PredicateType, List<Predicate>> predicateTypes = new HashMap<PredicateType, List<Predicate>>();
        private List<Predicate> predicates;
        
        private Criteria(Class<C> entityClass) {
            this(entityClass, null);
        }
        
        private Criteria(Class<C> entityClass, JoinType joinType) {
            builder = getEntityManager().getCriteriaBuilder();
            query = builder.createQuery(entityClass);
            root = query.from(entityClass);
            this.joinType = joinType;
            for (PredicateType type : PredicateType.values()) {
                predicateTypes.put(type, new LinkedList<Predicate>());
            }
        }
        
        // --------------------------------------------------------------------
        // Public criteria methods
        // --------------------------------------------------------------------
        
        public TypedQuery<C> createQuery() {
            collectPredicates();
            query.where(predicates.toArray(new Predicate[0]));
            return getEntityManager().createQuery(query);
        }
        
        public Criteria<C> or(Criteria<C> criteria) {
            criteria.collectPredicates();
            predicateTypes.get(PredicateType.OR).addAll(criteria.predicates);
            return this;
        }
        
        public <P> Criteria<C> join(SingularAttribute<? super C, P> att, Criteria<P> criteria) {
            if (joinType == null) {
                root.join(att);
            } else {
                root.join(att, joinType);
            }
            return join(criteria);
        }
        
        public <P> Criteria<C> join(ListAttribute<? super C, P> att, Criteria<P> criteria) {
            if (joinType == null) {
                root.join(att);
            } else {
                root.join(att, joinType);
            }
            return join(criteria);
        }
        
        public <P> Criteria<C> join(CollectionAttribute<? super C, P> att, Criteria<P> criteria) {
            if (joinType == null) {
                root.join(att);
            } else {
                root.join(att, joinType);
            }
            return join(criteria);
        }
        
        public <P> Criteria<C> join(SetAttribute<? super C, P> att, Criteria<P> criteria) {
            if (joinType == null) {
                root.join(att);
            } else {
                root.join(att, joinType);
            }
            return join(criteria);
        }
        
        public <P, K> Criteria<C> join(MapAttribute<? super C, K, P> att, Criteria<P> criteria) {
            if (joinType == null) {
                root.join(att);
            } else {
                root.join(att, joinType);
            }
            return join(criteria);
        }

        public Criteria<C> distinct() {
            query.distinct(true);
            return this;
        }
        
        // --------------------------------------------------------------------
        // Private criteria methods
        // --------------------------------------------------------------------
        
        private void add(Predicate pred) {
            predicateTypes.get(PredicateType.AND).add(pred);
        }
        
        private void collectPredicates() {
            predicates = new LinkedList<Predicate>();
            predicates.add(builder.and(predicateTypes.get(PredicateType.AND).toArray(new Predicate[0])));
            predicates.add(builder.or(predicateTypes.get(PredicateType.OR).toArray(new Predicate[0])));
            
        }
        
        private <P> Criteria<C> join(Criteria<P> criteria) {
            criteria.collectPredicates();
            predicateTypes.get(PredicateType.AND).addAll(criteria.predicates);
            return this;
        }
        
        // --------------------------------------------------------------------
        // Predicates
        // --------------------------------------------------------------------
        
        public <P> Criteria<C> eq(SingularAttribute<? super C, P> att, P value) {
            add(builder.equal(root.get(att), value));
            return this;
        }
        
        public <P> Criteria<C> notEq(SingularAttribute<? super C, P> att, P value) {
            add(builder.notEqual(root.get(att), value));
            return this;
        }
        
        public <P> Criteria<C> like(SingularAttribute<? super C, String> att, String value) {
            add(builder.like(root.get(att), value));
            return this;
        }
        
        public <P> Criteria<C> notLike(SingularAttribute<? super C, String> att, String value) {
            add(builder.notLike(root.get(att), value));
            return this;
        }
        
        public <P extends Number> Criteria<C> lt(SingularAttribute<? super C, P> att, P value) {
            add(builder.lt(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria<C> ltOrEq(SingularAttribute<? super C, P> att, P value) {
            add(builder.lessThanOrEqualTo(root.get(att), value));
            return this;
        }
        
        public <P extends Number> Criteria<C> gt(SingularAttribute<? super C, P> att, P value) {
            add(builder.gt(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria<C> gtOrEq(SingularAttribute<? super C, P> att, P value) {
            add(builder.greaterThanOrEqualTo(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria<C> between(SingularAttribute<? super C, P> att, P lower, P upper) {
            add(builder.between(root.get(att), lower, upper));
            return this;
        }
        
        public <P> Criteria<C> isNull(SingularAttribute<? super C, P> att) {
            add(builder.isNull(root.get(att)));
            return this;
        }
        
        public <P> Criteria<C> notNull(SingularAttribute<? super C, P> att) {
            add(builder.isNotNull(root.get(att)));
            return this;
        }
        
        public <P extends Collection<?>> Criteria<C> empty(SingularAttribute<? super C, P> att) {
            add(builder.isEmpty(root.get(att)));
            return this;
        }
        
        public <P extends Collection<?>> Criteria<C> notEmpty(SingularAttribute<? super C, P> att) {
            add(builder.isNotEmpty(root.get(att)));
            return this;
        }
        
    }

}
