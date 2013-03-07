package org.apache.deltaspike.query.impl.criteria.selection.strings;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.deltaspike.query.impl.criteria.selection.SingularAttributeSelection;


public class Upper<P> extends SingularAttributeSelection<P, String> {

    public Upper(SingularAttribute<P, String> attribute) {
        super(attribute);
    }

    @Override
    public <R> Selection<String> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return builder.upper(path.get(attribute));
    }

}
