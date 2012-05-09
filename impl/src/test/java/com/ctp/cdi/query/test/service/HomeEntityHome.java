package com.ctp.cdi.query.test.service;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.QueryResult;
import com.ctp.cdi.query.home.EntityHome;
import com.ctp.cdi.query.test.domain.Home;

@Named
@Stateful
//@ConversationScoped
public class HomeEntityHome extends EntityHome<Home, Long> {

    private static final long serialVersionUID = 1L;

    @Inject
    private HomeDao dao;
    
    private String name;

    @Override
    public EntityDao<Home, Long> getEntityDao() {
        return dao;
    }

    @Override
    protected QueryResult<Home> getQueryResult() {
        return dao.findByName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
