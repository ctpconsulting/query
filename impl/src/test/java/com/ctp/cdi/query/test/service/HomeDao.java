package com.ctp.cdi.query.test.service;

import java.io.Serializable;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.QueryResult;
import com.ctp.cdi.query.test.domain.Home;

@Dao
public abstract class HomeDao extends AbstractEntityDao<Home, Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract QueryResult<Home> findByName(String name);
    
}
