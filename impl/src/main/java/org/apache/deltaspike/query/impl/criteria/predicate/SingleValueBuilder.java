package org.apache.deltaspike.query.impl.criteria.predicate;

import javax.persistence.metamodel.SingularAttribute;


abstract class SingleValueBuilder<E, V> extends NoValueBuilder<E, V> {

    final V value;
    
    SingleValueBuilder(SingularAttribute<? super E, V> att, V value) {
        super(att);
        this.value = value;
    }

}
