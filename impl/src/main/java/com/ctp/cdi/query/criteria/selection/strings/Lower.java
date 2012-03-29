package com.ctp.cdi.query.criteria.selection.strings;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.selection.SingularAttributeSelection;

public class Lower<P> extends SingularAttributeSelection<P, String> {

    public Lower(SingularAttribute<P, String> attribute) {
        super(attribute);
    }

    @Override
    public <R> Selection<String> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return builder.lower(path.get(attribute));
    }

}
