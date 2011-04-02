package com.ctp.cdi.query;

import javax.persistence.EntityManager;


@Dao
public abstract class AbstractEntityDao<E, PK> implements EntityDao<E, PK> {

    protected abstract EntityManager getEntityManager();

}
