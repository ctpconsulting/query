package com.ctp.cdi.query.test.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.FirstResult;
import com.ctp.cdi.query.MaxResults;
import com.ctp.cdi.query.Modifying;
import com.ctp.cdi.query.Query;
import com.ctp.cdi.query.QueryParam;
import com.ctp.cdi.query.QueryResult;
import com.ctp.cdi.query.test.domain.Simple;

public abstract class SimpleDao extends AbstractEntityDao<Simple, Long> {

    public List<Simple> implementedQueryByName(String name) {
        String query = "select s from Simple s where s.name = :name";
        return getEntityManager().createQuery(query, Simple.class)
                .setParameter("name", name)
                .getResultList();
    }
    
    @Query(named = Simple.BY_NAME_ENABLED, max = 1)
    public abstract List<Simple> findByNamedQueryIndexed(String name, Boolean enabled);
    
    @Query(named = Simple.BY_NAME_ENABLED)
    public abstract List<Simple> findByNamedQueryRestricted(String name, Boolean enabled, 
            @MaxResults int max, @FirstResult Integer first);
    
    @Query(named = Simple.BY_ID, lock = LockModeType.PESSIMISTIC_WRITE)
    public abstract Simple findByNamedQueryNamed(
            @QueryParam("id") Long id, @QueryParam("enabled") Boolean enabled);
    
    @Query("select s from Simple s where s.name = ?1")
    public abstract Simple findByQuery(String name);
    
    @Query("select count(s) from Simple s where s.name = ?1")
    public abstract Long findCountByQuery(String name);
    
    public abstract Simple findByNameAndEnabled(String name, Boolean enabled);
    
    public abstract List<Simple> findByOrderByCounterAscIdDesc();
    
    @Query(sql = "SELECT * from SIMPLE_TABLE s WHERE s.name = ?1")
    public abstract List<Simple> findWithNative(String name);

    @Modifying @Query("update Simple as s set s.name = ?1 where s.id = ?2")
    public abstract int updateNameForId(String name, Long id);
    
    @Query(named = Simple.BY_NAME_LIKE)
    public abstract QueryResult<Simple> queryResultWithNamed(String name);
    
    public abstract QueryResult<Simple> findByName(String name);
    
    @Override
    protected abstract EntityManager getEntityManager();

}
