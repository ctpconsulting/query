package com.ctp.cdi.query.test.service;

import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.home.EntityHome;
import com.ctp.cdi.query.test.domain.Home;
import com.ctp.cdi.query.test.domain.Home_;

@Named
@Stateful
//@ConversationScoped
public class HomeEntityHome extends EntityHome<Home, Long> {

    private static final long serialVersionUID = 1L;

    @Inject
    private HomeDao dao;

    @Override
    public EntityDao<Home, Long> getEntityDao() {
        return dao;
    }

    @Override
    protected List<SingularAttribute<Home, ?>> searchAttributes() {
        return singularAttributes()
                .addIfNotEmpty(getSearch().getName(), Home_.name)
                .getAttributes();
    }

}
