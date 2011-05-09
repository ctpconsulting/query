package com.ctp.cdi.query.criteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

class JoinBuilder<P, R, E> implements PredicateBuilder<P> {
    
    final Criteria<R> criteria;
    final JoinType joinType;
    
    SingularAttribute<? super P, R> singular;
    ListAttribute<? super P, R> list;
    CollectionAttribute<? super P, R> collection;
    SetAttribute<? super P, R> set;
    MapAttribute<? super P, E, R> map;
    
    JoinBuilder(Criteria<R> criteria, JoinType joinType) {
        this.criteria = criteria;
        this.joinType = joinType;
    }
    
    JoinBuilder(Criteria<R> criteria, JoinType joinType, SingularAttribute<? super P, R> singular) {
        this(criteria, joinType);
        this.singular = singular;
    }
    
    JoinBuilder(Criteria<R> criteria, JoinType joinType, ListAttribute<? super P, R> list) {
        this(criteria, joinType);
        this.list = list;
    }
    
    JoinBuilder(Criteria<R> criteria, JoinType joinType, CollectionAttribute<? super P, R> collection) {
        this(criteria, joinType);
        this.collection = collection;
    }
    
    JoinBuilder(Criteria<R> criteria, JoinType joinType, SetAttribute<? super P, R> set) {
        this(criteria, joinType);
        this.set = set;
    }
    
    JoinBuilder(Criteria<R> criteria, JoinType joinType, MapAttribute<? super P, E, R> map) {
        this(criteria, joinType);
        this.map = map;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Predicate> build(CriteriaBuilder builder, Path<P> path) {
        Join join = null;
        if (singular != null) {
            join = joinSingular((From) path);
        } else if (list != null) {
            join = joinList((From) path);
        } else if (collection != null) {
            join = joinCollection((From) path);
        } else if (set != null) {
            join = joinSet((From) path);
        } else {
            join = joinMap((From) path);
        }
        return criteria.collectPredicates(builder, join);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Join joinSingular(From path) {
        if (joinType == null)
            return path.join(singular);
        return path.join(singular, joinType);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Join joinList(From path) {
        if (joinType == null)
            return path.join(list);
        return path.join(list, joinType);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Join joinCollection(From path) {
        if (joinType == null)
            return path.join(collection);
        return path.join(collection, joinType);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Join joinSet(From path) {
        if (joinType == null)
            return path.join(set);
        return path.join(set, joinType);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Join joinMap(From path) {
        if (joinType == null)
            return path.join(map);
        return path.join(map, joinType);
    }

}
