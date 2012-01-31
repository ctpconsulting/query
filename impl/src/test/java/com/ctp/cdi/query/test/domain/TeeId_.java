package com.ctp.cdi.query.test.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TeeId.class)
public abstract class TeeId_ {

    public static volatile SingularAttribute<Simple, Long> teeSetId;
    public static volatile SingularAttribute<Simple, Long> holeId;

}
