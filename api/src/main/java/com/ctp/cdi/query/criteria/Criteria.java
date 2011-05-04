package com.ctp.cdi.query.criteria;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

public class Criteria<C> {

    private EntityManager entityManager;
    private Class<C> entityClass;
    private JoinType joinType;
    private boolean distinct = false;
    
    private List<PredicateBuilder<C>> builders = new LinkedList<PredicateBuilder<C>>();
    
    public Criteria(Class<C> entityClass, EntityManager entityManager) {
        this(entityClass, entityManager, null);
    }
    
    public Criteria(Class<C> entityClass, EntityManager entityManager, JoinType joinType) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.joinType = joinType;
    }
    
    // --------------------------------------------------------------------
    // Public criteria methods
    // --------------------------------------------------------------------
    
    public TypedQuery<C> createQuery() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<C> query = builder.createQuery(entityClass);
        From<C, C> root = query.from(entityClass);
        List<Predicate> predicates = collectPredicates(builder, root);
        query.distinct(distinct);
        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query);
    }
    
    public Criteria<C> or(Criteria<C> criteria) {
        add(new OrBuilder<C>(criteria));
        return this;
    }
    
    public <P> Criteria<C> join(SingularAttribute<? super C, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P>(criteria, joinType, att));
        return this;
    }
    
    public <P> Criteria<C> join(ListAttribute<? super C, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P>(criteria, joinType, att));
        return this;
    }
    
    public <P> Criteria<C> join(CollectionAttribute<? super C, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P>(criteria, joinType, att));
        return this;
    }
    
    public <P> Criteria<C> join(SetAttribute<? super C, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P>(criteria, joinType, att));
        return this;
    }
    
    public <P> Criteria<C> join(MapAttribute<? super C, Object, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P>(criteria, joinType, att));
        return this;
    }

    public Criteria<C> distinct() {
        distinct = true;
        return this;
    }
    
    // --------------------------------------------------------------------
    // Package criteria methods
    // --------------------------------------------------------------------
    
    List<Predicate> collectPredicates(CriteriaBuilder builder, Path<C> path) {
        List<Predicate> predicates = new LinkedList<Predicate>();
        for (PredicateBuilder<C> pbuilder : builders) {
            List<Predicate> p = pbuilder.build(builder, path);
            predicates.addAll(p);
        }
        return predicates;
    }
    
    // --------------------------------------------------------------------
    // Private criteria methods
    // --------------------------------------------------------------------
    
    private void add(PredicateBuilder<C> pred) {
        builders.add(pred);
    }
    
    // --------------------------------------------------------------------
    // Predicates
    // --------------------------------------------------------------------
    
    public <P> Criteria<C> eq(SingularAttribute<? super C, P> att, P value) {
        add(new Eq<C, P>(att, value));
        return this;
    }
    
    public <P> Criteria<C> notEq(SingularAttribute<? super C, P> att, P value) {
        add(new NotEq<C, P>(att, value));
        return this;
    }
    
    public <P> Criteria<C> like(SingularAttribute<? super C, String> att, String value) {
        add(new Like<C>(att, value));
        return this;
    }
    
    public <P> Criteria<C> notLike(SingularAttribute<? super C, String> att, String value) {
        add(new NotLike<C>(att, value));
        return this;
    }
    
    public <P extends Number> Criteria<C> lt(SingularAttribute<? super C, P> att, P value) {
        add(new LessThan<C, P>(att, value));
        return this;
    }
    
    public <P extends Comparable<? super P>> Criteria<C> ltOrEq(SingularAttribute<? super C, P> att, P value) {
        add(new LessThanOrEqual<C, P>(att, value));
        return this;
    }
    
    public <P extends Number> Criteria<C> gt(SingularAttribute<? super C, P> att, P value) {
        add(new GreaterThan<C, P>(att, value));
        return this;
    }
    
    public <P extends Comparable<? super P>> Criteria<C> gtOrEq(SingularAttribute<? super C, P> att, P value) {
        add(new GreaterThanOrEqual<C, P>(att, value));
        return this;
    }
    
    public <P extends Comparable<? super P>> Criteria<C> between(SingularAttribute<? super C, P> att, P lower, P upper) {
        add(new Between<C, P>(att, lower, upper));
        return this;
    }
    
    public <P> Criteria<C> isNull(SingularAttribute<? super C, P> att) {
        add(new IsNull<C, P>(att));
        return this;
    }
    
    public <P> Criteria<C> notNull(SingularAttribute<? super C, P> att) {
        add(new IsNotNull<C, P>(att));
        return this;
    }
    
    public <P extends Collection<?>> Criteria<C> empty(SingularAttribute<? super C, P> att) {
        add(new IsEmpty<C, P>(att));
        return this;
    }
    
    public <P extends Collection<?>> Criteria<C> notEmpty(SingularAttribute<? super C, P> att) {
        add(new IsNotEmpty<C, P>(att));
        return this;
    }

}
