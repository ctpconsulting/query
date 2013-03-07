package org.apache.deltaspike.query.impl.criteria.selection.strings;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

public class SubstringFromTo<P> extends SubstringFrom<P> {

    private final int length;

    public SubstringFromTo(SingularAttribute<P, String> attribute, int from, int length) {
        super(attribute, from);
        this.length = length;
    }

    @Override
    public <R> Selection<String> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return builder.substring(path.get(attribute), from, length);
    }

}
