package com.ctp.cdi.query.test.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(NamedEntity.class)
public abstract class NamedEntity_ {

    public static volatile SingularAttribute<NamedEntity, Long> id;
    public static volatile SingularAttribute<NamedEntity, String> name;

}