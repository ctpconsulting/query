package com.ctp.cdi.query.test.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Home.class)
public class Home_ {

    public static volatile SingularAttribute<Home, Long> id;
    public static volatile SingularAttribute<Home, String> name;

}