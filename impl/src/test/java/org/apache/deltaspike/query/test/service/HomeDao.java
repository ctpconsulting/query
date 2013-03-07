package org.apache.deltaspike.query.test.service;

import org.apache.deltaspike.query.api.AbstractEntityDao;
import org.apache.deltaspike.query.api.QueryResult;
import org.apache.deltaspike.query.test.domain.Home;


public abstract class HomeDao extends AbstractEntityDao<Home, Long> {

    public abstract QueryResult<Home> findByName(String name);
    
}
