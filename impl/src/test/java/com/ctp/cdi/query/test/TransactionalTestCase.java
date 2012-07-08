package com.ctp.cdi.query.test;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class TransactionalTestCase {

    @Resource
    protected UserTransaction ut;
    
    @Before
    public void startTransaction() throws Exception {
        // temp fix - OpenJPA seems not to properly initialize the static
        // metamodel otherwise.
        getEntityManager().getMetamodel();
        ut.begin();
    }
    
    @After
    public void rollbackTransaction() throws Exception {
        ut.rollback();
    }
    
    protected abstract EntityManager getEntityManager();

}
