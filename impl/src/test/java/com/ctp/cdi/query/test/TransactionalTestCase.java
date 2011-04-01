package com.ctp.cdi.query.test;

import javax.annotation.Resource;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class TransactionalTestCase {

    @Resource
    private UserTransaction ut;
    
    @Before
    public void startTransaction() throws Exception {
        ut.begin();
    }
    
    @After
    public void rollbackTransaction() throws Exception {
        ut.rollback();
    }

}
