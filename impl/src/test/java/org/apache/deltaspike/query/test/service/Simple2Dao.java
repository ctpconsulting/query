package org.apache.deltaspike.query.test.service;

import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.api.EntityDao;
import org.apache.deltaspike.query.test.domain.Simple2;


@Dao
public interface Simple2Dao extends EntityDao<Simple2, Long> {

    Simple2 findByName(String name);

}
