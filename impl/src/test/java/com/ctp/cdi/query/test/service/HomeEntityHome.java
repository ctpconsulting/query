package com.ctp.cdi.query.test.service;

import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import com.ctp.cdi.query.home.EntityHome;
import com.ctp.cdi.query.test.domain.Home;

@Named
@Stateful
//@ConversationScoped
public class HomeEntityHome extends EntityHome<Home, Long> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
