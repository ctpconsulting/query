package com.ctp.cdi.query.criteria.predicate;

import javax.persistence.metamodel.SingularAttribute;

abstract class NoValueBuilder<E, V> implements PredicateBuilder<E> {

    final SingularAttribute<? super E, V> att;
    
    NoValueBuilder(SingularAttribute<? super E, V> att) {
        this.att = att;
    }

}
