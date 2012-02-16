package com.ctp.cdi.query.test.service;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.test.domain.mapped.MappedOne;

public abstract class MappedOneDao extends AbstractEntityDao<MappedOne, Long> {

    public abstract MappedOne findByName(String name);
    
}
