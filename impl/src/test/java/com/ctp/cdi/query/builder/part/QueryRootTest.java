package com.ctp.cdi.query.builder.part;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ctp.cdi.query.builder.MethodExpressionException;
import com.ctp.cdi.query.meta.DaoComponent;
import com.ctp.cdi.query.meta.DaoEntity;
import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.SimpleDao;


/**
 *
 * @author thomashug
 */
public class QueryRootTest {

    private final DaoComponent dao = new DaoComponent(SimpleDao.class, new DaoEntity(Simple.class, Long.class));
    
    @Test
    public void should_create_simple_query() {
        // given
        final String name = "findByName";
        final String expected = 
                "select e from Simple e " +
                "where e.name = ?1";
        
        // when
        String result = QueryRoot.create(name, dao).getJpqlQuery().trim();
        
        // then
        assertEquals(expected, result);
    }
    
    @Test
    public void should_create_complex_query() {
        // given
        final String name = "findByNameAndTemporalBetweenOrEnabledIsNull" +
        		"AndCamelCaseAndEmbedded_embeddNotEqualOrderByEmbedded_embeddDesc";
        final String expected = 
                "select e from Simple e " +
                "where e.name = ?1 " +
                "and e.temporal between ?2 and ?3 " +
                "or e.enabled IS NULL " +
                "and e.camelCase = ?4 " +
                "and e.embedded.embedd <> ?5 " +
                "order by e.embedded.embedd desc";
        
        // when
        String result = QueryRoot.create(name, dao).getJpqlQuery().trim();
        
        // then
        assertEquals(expected, result);
    }
    
    @Test
    public void should_create_query_with_order_by_only() {
        // given
        final String name = "findByOrderByIdAsc";
        final String expected = 
                "select e from Simple e " +
                "order by e.id asc";
        
        // when
        String result = QueryRoot.create(name, dao).getJpqlQuery().trim();
        
        // then
        assertEquals(expected, result);
    }
    
    @Test(expected = MethodExpressionException.class)
    public void should_fail_in_where() {
        // given
        final String name = "findByInvalid";
        
        // when
        QueryRoot.create(name, dao);
    }
    
    @Test(expected = MethodExpressionException.class)
    public void should_fail_with_prefix_only() {
        // given
        final String name = "findBy";
        
        // when
        QueryRoot.create(name, dao);
    }
    
    @Test(expected = MethodExpressionException.class)
    public void should_fail_in_order_by() {
        // given
        final String name = "findByNameOrderByInvalidDesc";
        
        // when
        QueryRoot.create(name, dao);
    }
    
}
