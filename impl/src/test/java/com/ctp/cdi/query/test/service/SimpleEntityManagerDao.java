package com.ctp.cdi.query.test.service;

import java.util.List;

import javax.persistence.EntityManager;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.test.domain.Simple3;

@Dao(Simple3.class)
public interface SimpleEntityManagerDao extends EntityManager {
    
    List<Simple3> findByName(String name);
    
}
