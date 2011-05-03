package com.ctp.cdi.query;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    protected abstract EntityManager getEntityManager();

    protected abstract CriteriaQuery<E> criteriaQuery();
    
    protected abstract Class<E> entityClass();
    
    protected Criteria<E> criteria() {
        return new Criteria<E>(entityClass());
    }
    
    protected <T> Criteria<T> criteria(Class<T> clazz) {
        return new Criteria<T>(clazz);
    }
    
    protected class Criteria<C> {
        
        private CriteriaBuilder builder;
        private CriteriaQuery<C> query;
        private Root<C> root;
        
        List<Predicate> predicate = new LinkedList<Predicate>();
        
        private Criteria(Class<C> entityClass) {
            builder = getEntityManager().getCriteriaBuilder();
            query = builder.createQuery(entityClass);
            root = query.from(entityClass);
        }
        
        public TypedQuery<C> createQuery() {
            query.where(builder.and(predicate.toArray(new Predicate[] {})));
            return getEntityManager().createQuery(query);
        } 
        
        private void add(Predicate pred) {
            predicate.add(pred);
        }
        
        public <P> Criteria<C> eq(SingularAttribute<C, P> att, P value) {
            add(builder.equal(root.get(att), value));
            return this;
        }
        
        public <P> Criteria<C> notEq(SingularAttribute<C, P> att, P value) {
            add(builder.notEqual(root.get(att), value));
            return this;
        }
        
        public <P> Criteria<C> like(SingularAttribute<C, String> att, String value) {
            add(builder.like(root.get(att), value));
            return this;
        }
        
        public <P> Criteria<C> notLike(SingularAttribute<C, String> att, String value) {
            add(builder.notLike(root.get(att), value));
            return this;
        }
        
        public <P extends Number> Criteria<C> lt(SingularAttribute<C, P> att, P value) {
            add(builder.lt(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria<C> ltOrEq(SingularAttribute<C, P> att, P value) {
            add(builder.lessThanOrEqualTo(root.get(att), value));
            return this;
        }
        
        public <P extends Number> Criteria<C> gt(SingularAttribute<C, P> att, P value) {
            add(builder.gt(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria<C> gtOrEq(SingularAttribute<C, P> att, P value) {
            add(builder.greaterThanOrEqualTo(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria<C> between(SingularAttribute<C, P> att, P lower, P upper) {
            add(builder.between(root.get(att), lower, upper));
            return this;
        }
        
        public <P> Criteria<C> isNull(SingularAttribute<C, P> att) {
            add(builder.isNull(root.get(att)));
            return this;
        }
        
        public <P> Criteria<C> notNull(SingularAttribute<C, P> att) {
            add(builder.isNotNull(root.get(att)));
            return this;
        }
        
        public <P extends Collection<?>> Criteria<C> empty(SingularAttribute<C, P> att) {
            add(builder.isEmpty(root.get(att)));
            return this;
        }
        
        public <P extends Collection<?>> Criteria<C> notEmpty(SingularAttribute<C, P> att) {
            add(builder.isNotEmpty(root.get(att)));
            return this;
        }
        
    }

}
