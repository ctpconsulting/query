package org.apache.deltaspike.query.test.service;

import java.util.List;

import org.apache.deltaspike.query.api.AbstractEntityDao;
import org.apache.deltaspike.query.api.criteria.CriteriaSupport;
import org.apache.deltaspike.query.test.domain.Simple;
import org.apache.deltaspike.query.test.domain.Simple_;

public abstract class SimpleCriteriaDao extends AbstractEntityDao<Simple, Long>
        implements CriteriaSupport<Simple> {

    public List<Simple> queryByCriteria(String name, Boolean enabled, Integer from, Integer to) {
        return criteria()
                .eq(Simple_.name, name)
                .eq(Simple_.enabled, enabled)
                .between(Simple_.counter, from, to)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public Statistics queryWithSelect(String name) {
        return criteria()
                 .select(Statistics.class, avg(Simple_.counter), count(Simple_.counter))
                 .eq(Simple_.name, name)
                 .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public Object[] queryWithSelectAggregateReturnArray(String name) {
        return criteria()
                 .select(min(Simple_.counter), max(Simple_.counter),
                         currDate(), currTime(), currTStamp())
                 .eq(Simple_.name, name)
                 .createQuery()
                 .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> queryWithSelectAttributes(String name) {
        return criteria()
                 .select(attribute(Simple_.name),
                         upper(Simple_.name), lower(Simple_.name),
                         substring(Simple_.name, 2), substring(Simple_.name, 2, 2))
                 .eq(Simple_.name, name)
                 .createQuery()
                 .getResultList();
    }

}
