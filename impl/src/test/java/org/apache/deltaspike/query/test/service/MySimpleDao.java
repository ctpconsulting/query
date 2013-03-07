package org.apache.deltaspike.query.test.service;

import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.api.EntityDao;
import org.apache.deltaspike.query.test.domain.Simple;


@Dao
public interface MySimpleDao extends MyEntityDao<Simple>, EntityDao<Simple, Long> {
}
