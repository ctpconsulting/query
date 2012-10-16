package com.ctp.cdi.query.test.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.criteria.CriteriaSupport;
import com.ctp.cdi.query.test.domain.OneToMany;
import com.ctp.cdi.query.test.domain.OneToMany_;
import com.ctp.cdi.query.test.domain.OneToOne;
import com.ctp.cdi.query.test.domain.OneToOne_;
import com.ctp.cdi.query.test.domain.Parent;
import com.ctp.cdi.query.test.domain.Parent_;

public abstract class ParentDao extends AbstractEntityDao<Parent, Long> 
        implements CriteriaSupport<Parent> {

    public List<Parent> joinQuery(String name, String oneName, String manyName) {
        return criteria()
                .eq(Parent_.name, name)
                .join(Parent_.one, 
                        where(OneToOne.class, JoinType.LEFT)
                            .eq(OneToOne_.name, oneName)
                )
                .join(Parent_.many,
                        where(OneToMany.class)
                            .eq(OneToMany_.name, manyName)
                )
                .createQuery()
                .getResultList();
    }
    
    public List<Parent> nullAwareQuery(String name1, String name2, Long counter) {
        return criteria()
                .eq(Parent_.name, name1)
                .eq(Parent_.name, name2)
                .eq(Parent_.value, counter)
                .createQuery()
                .getResultList();
    }
    
    public Parent fetchQuery(String name) {
        return criteria()
                .eq(Parent_.name, name)
                .fetch(Parent_.many)
                .distinct()
                .createQuery()
                .getSingleResult();
    }
    
    public List<Parent> fetchByName(String name1, String name2, String name3) {
        return criteria()
                .in(Parent_.name, name1, name2, name3)
                .createQuery()
                .getResultList();
    }
    
    public List<Parent> orQuery(String name1, String name2) {
        return criteria()
                .or(
                    criteria()
                        .eq(Parent_.name, name2)
                        .between(Parent_.value, 50L, 100L),
                    criteria()
                        .eq(Parent_.name, name1)
                        .between(Parent_.value, 0L, 50L),
                    criteria()
                        .eq(Parent_.name, "does not exist!")
                )
                .createQuery()
                .getResultList();
    }
    
    public List<Parent> orderedQuery() {
        return criteria()
                .orderAsc(Parent_.name)
                .createQuery()
                .getResultList();
    }

}
