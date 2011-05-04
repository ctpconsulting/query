package com.ctp.cdi.query.test.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Parent.class)
public abstract class Parent_ extends NamedEntity_ {

    public static volatile SingularAttribute<Parent, OneToOne> one;
    public static volatile ListAttribute<Parent, OneToMany> many;
    public static volatile SingularAttribute<Parent, Long> value;

}
