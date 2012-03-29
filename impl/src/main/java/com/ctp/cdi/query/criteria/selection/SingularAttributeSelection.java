package com.ctp.cdi.query.criteria.selection;

import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.QuerySelection;

public abstract class SingularAttributeSelection<P, X> implements QuerySelection<P, X> {

    protected final SingularAttribute<P, X> attribute;

    public SingularAttributeSelection(SingularAttribute<P, X> attribute) {
        this.attribute = attribute;
    }

}
