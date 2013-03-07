package org.apache.deltaspike.query.test.service;

import java.util.List;

import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.api.EntityDao;
import org.apache.deltaspike.query.api.WithEntityManager;
import org.apache.deltaspike.query.test.domain.Simple;


@Dao
@WithEntityManager(Simplistic.class)
public interface SimpleDaoWithEntityManager extends EntityDao<Simple, Long> {

    List<Simple> findByName(String name);

}
