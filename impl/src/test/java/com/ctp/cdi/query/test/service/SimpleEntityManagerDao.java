package com.ctp.cdi.query.test.service;

import java.util.List;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.EntityManagerDao;
import com.ctp.cdi.query.test.domain.Simple3;

@Dao(Simple3.class)
public interface SimpleEntityManagerDao extends EntityManagerDao<Simple3, Long> {

    List<Simple3> findByName(String name);

}
