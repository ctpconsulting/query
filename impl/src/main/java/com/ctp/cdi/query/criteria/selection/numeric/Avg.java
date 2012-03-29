package com.ctp.cdi.query.criteria.selection.numeric;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.selection.SingularAttributeSelection;

public class Avg<P, X extends Number> extends SingularAttributeSelection<P, X> {

    public Avg(SingularAttribute<P, X> attribute) {
        super(attribute);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Selection<X> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return (Selection<X>) builder.avg(path.get(attribute));
    }
}
