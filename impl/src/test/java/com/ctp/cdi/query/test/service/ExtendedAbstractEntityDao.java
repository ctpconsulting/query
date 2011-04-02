package com.ctp.cdi.query.test.service;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.test.domain.Simple;
import java.util.List;

public abstract class ExtendedAbstractEntityDao extends AbstractEntityDao<Simple, Long> {

    public List<Simple> implementedQueryByName(String name) {
        String query = "select s from Simple s where s.name = :name";
        return getEntityManager().createQuery(query)
                .setParameter("name", name)
                .getResultList();
    }
}
