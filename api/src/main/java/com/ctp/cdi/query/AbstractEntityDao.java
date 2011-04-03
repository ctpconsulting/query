package com.ctp.cdi.query;

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


@Dao
public abstract class AbstractEntityDao<E, PK> implements EntityDao<E, PK> {

    protected abstract EntityManager getEntityManager();

    protected abstract CriteriaQuery<E> criteriaQuery();
    
    protected abstract Class<E> entityClass();
    
    protected Criteria criteria() {
        return new Criteria();
    }
    
    protected class Criteria {
        
        private CriteriaBuilder builder;
        private CriteriaQuery<E> query;
        private Root<E> root;
        
        private boolean isAnd = true;
        
        List<Predicate> and = new LinkedList<Predicate>();
        List<Predicate> or = new LinkedList<Predicate>();
        
        private Criteria() {
            Class<E> entityClass = entityClass();
            builder = getEntityManager().getCriteriaBuilder();
            query = builder.createQuery(entityClass);
            root = query.from(entityClass);
        }
        
        public TypedQuery<E> createQuery() {
            query.where(
                    builder.and(and.toArray(new Predicate[] {})), 
                    builder.or(or.toArray(new Predicate[] {})));
            return getEntityManager().createQuery(query);
        } 
        
        private void add(Predicate pred) {
            if (isAnd)
                and.add(pred);
            else
                or.add(pred);
        }
        
        public Criteria and() {
            isAnd = true;
            return this;
        }
        
        public Criteria or() {
            isAnd = false;
            return this;
        }
        
        public <P> Criteria eq(SingularAttribute<E, P> att, P value) {
            add(builder.equal(root.get(att), value));
            return this;
        }
        
        public <P> Criteria notEq(SingularAttribute<E, P> att, P value) {
            add(builder.notEqual(root.get(att), value));
            return this;
        }
        
        public <P> Criteria like(SingularAttribute<E, String> att, String value) {
            add(builder.like(root.get(att), value));
            return this;
        }
        
        public <P> Criteria notLike(SingularAttribute<E, String> att, String value) {
            add(builder.notLike(root.get(att), value));
            return this;
        }
        
        public <P extends Number> Criteria lt(SingularAttribute<E, P> att, P value) {
            add(builder.lt(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria ltOrEq(SingularAttribute<E, P> att, P value) {
            add(builder.lessThanOrEqualTo(root.get(att), value));
            return this;
        }
        
        public <P extends Number> Criteria gt(SingularAttribute<E, P> att, P value) {
            add(builder.gt(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria gtOrEq(SingularAttribute<E, P> att, P value) {
            add(builder.greaterThanOrEqualTo(root.get(att), value));
            return this;
        }
        
        public <P extends Comparable<? super P>> Criteria between(SingularAttribute<E, P> att, P lower, P upper) {
            add(builder.between(root.get(att), lower, upper));
            return this;
        }
        
        public <P> Criteria isNull(SingularAttribute<E, P> att) {
            add(builder.isNull(root.get(att)));
            return this;
        }
        
        public <P> Criteria notNull(SingularAttribute<E, P> att) {
            add(builder.isNotNull(root.get(att)));
            return this;
        }
        
        public <P extends Collection<?>> Criteria empty(SingularAttribute<E, P> att) {
            add(builder.isEmpty(root.get(att)));
            return this;
        }
        
        public <P extends Collection<?>> Criteria notEmpty(SingularAttribute<E, P> att) {
            add(builder.isNotEmpty(root.get(att)));
            return this;
        }
        
    }

}
