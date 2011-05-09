package com.ctp.cdi.query.criteria;

import java.util.Arrays;
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
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.OrderBy.OrderDirection;

public class Criteria<C> {

    private EntityManager entityManager;
    private Class<C> entityClass;
    private JoinType joinType;
    private boolean ignoreNull = true;
    private boolean distinct = false;
    
    private List<PredicateBuilder<C>> builders = new LinkedList<PredicateBuilder<C>>();
    private List<QueryProcessor<C>> processors = new LinkedList<QueryProcessor<C>>();
    
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
        applyProcessors(query, builder, root);
        return entityManager.createQuery(query);
    }
    
    @SuppressWarnings("unchecked")
    public Criteria<C> or(Criteria<C> first, Criteria<C> second) {
        return internalOr(first, second);
    }
    
    @SuppressWarnings("unchecked")
    public Criteria<C> or(Criteria<C> first, Criteria<C> second, Criteria<C> third) {
        return internalOr(first, second, third);
    }
    
    @SuppressWarnings("unchecked")
    public Criteria<C> or(Collection<Criteria<C>> criteria) {
        return internalOr(criteria.toArray(new Criteria[0]));
    }
    
    public <P, E> Criteria<C> join(SingularAttribute<? super C, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C> join(ListAttribute<? super C, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C> join(CollectionAttribute<? super C, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C> join(SetAttribute<? super C, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C> join(MapAttribute<? super C, E, P> att, Criteria<P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C> fetch(SingularAttribute<? super C, P> att) {
        add(new FetchBuilder<C, P, E>(att, null));
        return this;
    }
    
    public <P, E> Criteria<C> fetch(SingularAttribute<? super C, P> att, JoinType joinType) {
        add(new FetchBuilder<C, P, E>(att, joinType));
        return this;
    }
    
    public <P, E> Criteria<C> fetch(PluralAttribute<? super C, P, E> att) {
        add(new FetchBuilder<C, P, E>(att, null));
        return this;
    }
    
    public <P, E> Criteria<C> fetch(PluralAttribute<? super C, P, E> att, JoinType joinType) {
        add(new FetchBuilder<C, P, E>(att, joinType));
        return this;
    }
    
    public <P> Criteria<C> asc(SingularAttribute<? super C, P> att) {
        add(new OrderBy<C, P>(att, OrderDirection.ASC));
        return this;
    }
    
    public <P> Criteria<C> desc(SingularAttribute<? super C, P> att) {
        add(new OrderBy<C, P>(att, OrderDirection.DESC));
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
    
    void applyProcessors(CriteriaQuery<C> query, CriteriaBuilder builder, From<C, C> from) {
        for (QueryProcessor<C> proc : processors) {
            proc.process(query, builder, from);
        }
    }
    
    @SuppressWarnings("unchecked")
    Criteria<C> internalOr(Criteria<C>... other) {
        List<Criteria<C>> list = new LinkedList<Criteria<C>>();
        list.addAll(Arrays.asList(other));
        add(new OrBuilder<C>(list.toArray(new Criteria[0])));
        return this;
    }
    
    // --------------------------------------------------------------------
    // Private criteria methods
    // --------------------------------------------------------------------
    
    private void add(PredicateBuilder<C> pred) {
        builders.add(pred);
    }
    
    private <P> void add(PredicateBuilder<C> pred, P value) {
        if (ignoreNull && value != null) {
            builders.add(pred);
        } else if (!ignoreNull) {
            builders.add(pred);
        }
    }
    
    private void add(QueryProcessor<C> proc) {
        processors.add(proc);
    }
    
    // --------------------------------------------------------------------
    // Predicates
    // --------------------------------------------------------------------
    
    public <P> Criteria<C> eq(SingularAttribute<? super C, P> att, P value) {
        add(new Eq<C, P>(att, value), value);
        return this;
    }
    
    public <P> Criteria<C> notEq(SingularAttribute<? super C, P> att, P value) {
        add(new NotEq<C, P>(att, value), value);
        return this;
    }
    
    public <P> Criteria<C> like(SingularAttribute<? super C, String> att, String value) {
        add(new Like<C>(att, value), value);
        return this;
    }
    
    public <P> Criteria<C> notLike(SingularAttribute<? super C, String> att, String value) {
        add(new NotLike<C>(att, value), value);
        return this;
    }
    
    public <P extends Number> Criteria<C> lt(SingularAttribute<? super C, P> att, P value) {
        add(new LessThan<C, P>(att, value), value);
        return this;
    }
    
    public <P extends Comparable<? super P>> Criteria<C> ltOrEq(SingularAttribute<? super C, P> att, P value) {
        add(new LessThanOrEqual<C, P>(att, value), value);
        return this;
    }
    
    public <P extends Number> Criteria<C> gt(SingularAttribute<? super C, P> att, P value) {
        add(new GreaterThan<C, P>(att, value), value);
        return this;
    }
    
    public <P extends Comparable<? super P>> Criteria<C> gtOrEq(SingularAttribute<? super C, P> att, P value) {
        add(new GreaterThanOrEqual<C, P>(att, value), value);
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
    
    public <P> Criteria<C> in(SingularAttribute<? super C, P> att, P... values) {
        add(new In<C, P>(att, values), values);
        return this;
    }

}
