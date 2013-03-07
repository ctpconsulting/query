package org.apache.deltaspike.query.test.service;

import java.util.List;

import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.api.EntityManagerDao;
import org.apache.deltaspike.query.test.domain.Simple3;


@Dao(Simple3.class)
public interface SimpleEntityManagerDao extends EntityManagerDao<Simple3, Long> {

    List<Simple3> findByName(String name);

}
