package com.ctp.cdi.query.test.service;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.QueryResult;
import com.ctp.cdi.query.test.domain.Home;

public abstract class HomeDao extends AbstractEntityDao<Home, Long> {

    public abstract QueryResult<Home> findByName(String name);
    
}
