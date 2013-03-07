package org.apache.deltaspike.query.impl.criteria.selection;

import javax.persistence.metamodel.SingularAttribute;

import org.apache.deltaspike.query.api.criteria.QuerySelection;


public abstract class SingularAttributeSelection<P, X> implements QuerySelection<P, X> {

    protected final SingularAttribute<P, X> attribute;

    public SingularAttributeSelection(SingularAttribute<P, X> attribute) {
        this.attribute = attribute;
    }

}
