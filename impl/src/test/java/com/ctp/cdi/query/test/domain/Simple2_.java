package com.ctp.cdi.query.test.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Simple2.class)
public class Simple2_ {

    public static volatile SingularAttribute<Simple, Long> id;
    public static volatile SingularAttribute<Simple, String> name;

}
