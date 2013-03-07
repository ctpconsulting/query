package org.apache.deltaspike.query.impl.criteria.selection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;


public class AttributeQuerySelection<P, X> extends SingularAttributeSelection<P, X> {
    
    public AttributeQuerySelection(SingularAttribute<P, X> attribute) {
        super(attribute);
    }

    @Override
    public <R> Selection<X> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return path.get(attribute);
    }

}
