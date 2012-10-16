package com.ctp.cdi.query.test.service;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.criteria.QueryDslSupport;
import com.ctp.cdi.query.test.domain.Simple;

@Dao
public abstract class SimpleQueryDslDao 
    implements EntityDao<Simple, Long>, QueryDslSupport {
}
