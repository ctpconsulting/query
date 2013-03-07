package org.apache.deltaspike.query.test.service;

import org.apache.deltaspike.query.api.AbstractEntityDao;
import org.apache.deltaspike.query.test.domain.mapped.MappedOne;


public abstract class MappedOneDao extends AbstractEntityDao<MappedOne, Long> {

    public abstract MappedOne findByName(String name);
    
}
