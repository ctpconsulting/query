package com.ctp.cdi.query.criteria.predicate;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

public class FetchBuilder<P, R, E> implements PredicateBuilder<P> {
    
    final JoinType joinType;
    
    SingularAttribute<? super P, R> singular;
    PluralAttribute<? super P, R, E> plural;

    public FetchBuilder(SingularAttribute<? super P, R> singular, JoinType joinType) {
        this.joinType = joinType;
        this.singular = singular;
    }

    public FetchBuilder(PluralAttribute<? super P, R, E> plural, JoinType joinType) {
        this.joinType = joinType;
        this.plural = plural;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<P> path) {
        if (singular != null) {
            fetchSingular((From) path);
        } else if (plural != null) {
            fetchPlural((From) path);
        }
        return Collections.emptyList();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void fetchSingular(From path) {
        if (joinType == null)
            path.fetch(singular);
        else
            path.fetch(singular, joinType);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void fetchPlural(From path) {
        if (joinType == null)
            path.fetch(plural);
        else
            path.fetch(plural, joinType);
    }

}
