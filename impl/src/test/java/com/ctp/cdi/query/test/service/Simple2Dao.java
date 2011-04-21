package com.ctp.cdi.query.test.service;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.test.domain.Simple2;

public abstract class Simple2Dao extends AbstractEntityDao<Simple2, Long> {

    public abstract Simple2 findByName(String name);

}
