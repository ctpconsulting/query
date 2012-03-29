package com.ctp.cdi.query.criteria.selection.numeric;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.selection.SingularAttributeSelection;

public class Count<P, N extends Number> extends SingularAttributeSelection<P, N> {

    public Count(SingularAttribute<P, N> attribute) {
        super(attribute);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Selection<N> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return (Selection<N>) builder.count(path.get(attribute));
    }
}
