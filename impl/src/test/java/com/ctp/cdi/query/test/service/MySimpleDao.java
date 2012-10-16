package com.ctp.cdi.query.test.service;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.test.domain.Simple;

@Dao
public interface MySimpleDao extends MyEntityDao<Simple>, EntityDao<Simple, Long> {
}
