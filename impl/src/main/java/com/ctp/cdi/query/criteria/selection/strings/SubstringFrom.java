package com.ctp.cdi.query.criteria.selection.strings;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.selection.SingularAttributeSelection;

public class SubstringFrom<P> extends SingularAttributeSelection<P, String> {

    final int from;

    public SubstringFrom(SingularAttribute<P, String> attribute, int from) {
        super(attribute);
        this.from = from;
    }

    @Override
    public <R> Selection<String> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return builder.substring(path.get(attribute), from);
    }

}
