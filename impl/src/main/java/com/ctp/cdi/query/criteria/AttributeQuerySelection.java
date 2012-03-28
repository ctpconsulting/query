package com.ctp.cdi.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;


public class AttributeQuerySelection<P, X> implements QuerySelection<P, X> {
    
    private final SingularAttribute<? super P, X> attribute;
    
    public AttributeQuerySelection(SingularAttribute<P, X> attribute) {
        this.attribute = attribute;
    }

    @Override
    public <R> Selection<X> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return path.get(attribute);
    }

}
