package com.ctp.cdi.query.test.service;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.test.domain.Simple2;

@Dao
public interface Simple2Dao extends EntityDao<Simple2, Long> {

    Simple2 findByName(String name);

}
