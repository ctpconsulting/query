package com.ctp.cdi.query.test.service;

import java.util.List;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.WithEntityManager;
import com.ctp.cdi.query.test.domain.Simple;

@Dao
@WithEntityManager(Simplistic.class)
public interface SimpleDaoWithEntityManager extends EntityDao<Simple, Long> {

    List<Simple> findByName(String name);

}
