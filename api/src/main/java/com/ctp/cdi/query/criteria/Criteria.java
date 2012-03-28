package com.ctp.cdi.query.criteria;

import java.util.ArrayList;
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
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.OrderBy.OrderDirection;

public class Criteria<C, R> {

    private EntityManager entityManager;
    private Class<C> entityClass;
    private Class<R> resultClass;
    private JoinType joinType;
    private final boolean ignoreNull = true;
    private boolean distinct = false;
    
    private final List<PredicateBuilder<C>> builders = new LinkedList<PredicateBuilder<C>>();
    private final List<QueryProcessor<C>> processors = new LinkedList<QueryProcessor<C>>();
    private final List<QuerySelection<C, ?>> selections = new LinkedList<QuerySelection<C, ?>>();
    
    public Criteria(Class<C> entityClass, Class<R> resultClass, EntityManager entityManager) {
        this(entityClass, resultClass, entityManager, null);
    }
    
    public Criteria(Class<C> entityClass, Class<R> resultClass, EntityManager entityManager, JoinType joinType) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.resultClass = resultClass;
        this.joinType = joinType;
    }
    
    // --------------------------------------------------------------------
    // Public criteria methods
    // --------------------------------------------------------------------
    
    public TypedQuery<R> createQuery() {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<R> query = builder.createQuery(resultClass);
            From<C, C> root = query.from(entityClass);
            if (!selections.isEmpty()) {
                query.multiselect(prepareSelections(query, builder, root));
            }
            List<Predicate> predicates = collectPredicates(builder, root);
            query.distinct(distinct);
            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }
            applyProcessors(query, builder, root);
            return entityManager.createQuery(query);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public Criteria<C, R> or(Criteria<C, R> first, Criteria<C, R> second) {
        return internalOr(first, second);
    }
    
    @SuppressWarnings("unchecked")
    public Criteria<C, R> or(Criteria<C, R> first, Criteria<C, R> second, Criteria<C, R> third) {
        return internalOr(first, second, third);
    }
    
    @SuppressWarnings("unchecked")
    public Criteria<C, R> or(Collection<Criteria<C, R>> criteria) {
        return internalOr(criteria.toArray(new Criteria[0]));
    }
    
    public <P, E> Criteria<C, R> join(SingularAttribute<? super C, P> att, Criteria<P, P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C, R> join(ListAttribute<? super C, P> att, Criteria<P, P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C, R> join(CollectionAttribute<? super C, P> att, Criteria<P, P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C, R> join(SetAttribute<? super C, P> att, Criteria<P, P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C, R> join(MapAttribute<? super C, E, P> att, Criteria<P, P> criteria) {
        add(new JoinBuilder<C, P, E>(criteria, joinType, att));
        return this;
    }
    
    public <P, E> Criteria<C, R> fetch(SingularAttribute<? super C, P> att) {
        add(new FetchBuilder<C, P, E>(att, null));
        return this;
    }
    
    public <P, E> Criteria<C, R> fetch(SingularAttribute<? super C, P> att, JoinType joinType) {
        add(new FetchBuilder<C, P, E>(att, joinType));
        return this;
    }
    
    public <P, E> Criteria<C, R> fetch(PluralAttribute<? super C, P, E> att) {
        add(new FetchBuilder<C, P, E>(att, null));
        return this;
    }
    
    public <P, E> Criteria<C, R> fetch(PluralAttribute<? super C, P, E> att, JoinType joinType) {
        add(new FetchBuilder<C, P, E>(att, joinType));
        return this;
    }
    
    public <P> Criteria<C, R> orderAsc(SingularAttribute<? super C, P> att) {
        add(new OrderBy<C, P>(att, OrderDirection.ASC));
        return this;
    }
    
    public <P> Criteria<C, R> orderDesc(SingularAttribute<? super C, P> att) {
        add(new OrderBy<C, P>(att, OrderDirection.DESC));
        return this;
    }

    public Criteria<C, R> distinct() {
        distinct = true;
        return this;
    }
    
    public <N> Criteria<C, N> select(Class<N> resultClass, QuerySelection<C, ?>... selection) {
        Criteria<C, N> result = new Criteria<C, N>(entityClass, resultClass, entityManager, joinType);
        result.builders.addAll(this.builders);
        result.distinct = this.distinct;
        result.processors.addAll(this.processors);
        result.selections.addAll(Arrays.asList(selection));
        return result;
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
    
    void applyProcessors(CriteriaQuery<R> query, CriteriaBuilder builder, From<C, C> from) {
        for (QueryProcessor<C> proc : processors) {
            proc.process(query, builder, from);
        }
    }
    
    @SuppressWarnings("unchecked")
    Criteria<C, R> internalOr(Criteria<C, R>... other) {
        List<Criteria<C, R>> list = new LinkedList<Criteria<C, R>>();
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
    
    private Selection<?>[] prepareSelections(CriteriaQuery<R> query, CriteriaBuilder builder, From<C, C> root) {
        List<Selection<?>> result = new ArrayList<Selection<?>>(selections.size());
        for (QuerySelection<C, ?> selection : selections) {
            result.add(selection.toSelection(query, builder, root));
        }
        return result.toArray(new Selection<?>[]{});
    }
    
    // --------------------------------------------------------------------
    // Predicates
    // --------------------------------------------------------------------
    
    public <P> Criteria<C, R> eq(SingularAttribute<? super C, P> att, P value) {
        add(new Eq<C, P>(att, value), value);
        return this;
    }
    
    public <P> Criteria<C, R> notEq(SingularAttribute<? super C, P> att, P value) {
        add(new NotEq<C, P>(att, value), value);
        return this;
    }
    
    public <P> Criteria<C, R> like(SingularAttribute<? super C, String> att, String value) {
        add(new Like<C>(att, value), value);
        return this;
    }
    
    public <P> Criteria<C, R> notLike(SingularAttribute<? super C, String> att, String value) {
        add(new NotLike<C>(att, value), value);
        return this;
    }
    
    public <P extends Number> Criteria<C, R> lt(SingularAttribute<? super C, P> att, P value) {
        add(new LessThan<C, P>(att, value), value);
        return this;
    }
    
    public <P extends Comparable<? super P>> Criteria<C, R> ltOrEq(SingularAttribute<? super C, P> att, P value) {
        add(new LessThanOrEqual<C, P>(att, value), value);
        return this;
    }
    
    public <P extends Number> Criteria<C, R> gt(SingularAttribute<? super C, P> att, P value) {
        add(new GreaterThan<C, P>(att, value), value);
        return this;
    }
    
    public <P extends Comparable<? super P>> Criteria<C, R> gtOrEq(SingularAttribute<? super C, P> att, P value) {
        add(new GreaterThanOrEqual<C, P>(att, value), value);
        return this;
    }
    
    public <P extends Comparable<? super P>> Criteria<C, R> between(SingularAttribute<? super C, P> att, P lower, P upper) {
        add(new Between<C, P>(att, lower, upper));
        return this;
    }
    
    public <P> Criteria<C, R> isNull(SingularAttribute<? super C, P> att) {
        add(new IsNull<C, P>(att));
        return this;
    }
    
    public <P> Criteria<C, R> notNull(SingularAttribute<? super C, P> att) {
        add(new IsNotNull<C, P>(att));
        return this;
    }
    
    public <P extends Collection<?>> Criteria<C, R> empty(SingularAttribute<? super C, P> att) {
        add(new IsEmpty<C, P>(att));
        return this;
    }
    
    public <P extends Collection<?>> Criteria<C, R> notEmpty(SingularAttribute<? super C, P> att) {
        add(new IsNotEmpty<C, P>(att));
        return this;
    }
    
    public <P> Criteria<C, R> in(SingularAttribute<? super C, P> att, P... values) {
        add(new In<C, P>(att, values), values);
        return this;
    }

}
