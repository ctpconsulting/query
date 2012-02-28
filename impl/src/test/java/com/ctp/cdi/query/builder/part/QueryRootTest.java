package com.ctp.cdi.query.builder.part;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;


/**
 *
 * @author thomashug
 */
public class QueryRootTest {
    
    @Test
    public void shouldCreateSimpleQuery() {
        // given
        final String name = "findByName";
        final String expected = 
                "select e from Simple e " +
                "where e.name = ?1";
        
        // when
        String result = QueryRoot.create(name, "Simple").getJpqlQuery().trim();
        
        // then
        assertEquals(expected, result);
    }
    
    @Test
    public void shouldCreateComplexQuery() {
        // given
        final String name = "findByNameAndCreatedBetweenOrNullableIsNullAndCamelCaseOrderByCreatedDesc";
        final String expected = 
                "select e from Simple e " +
                "where e.name = ?1 " +
                "and e.created between ?2 and ?3 " +
                "or e.nullable IS NULL " +
                "and e.camelCase = ?4 " +
                "order by e.created desc";
        
        // when
        String result = QueryRoot.create(name, "Simple").getJpqlQuery().trim();
        
        // then
        assertEquals(expected, result);
    }
    
    @Test
    public void shouldCreateQueryWithOrderByOnly() {
        // given
        final String name = "findByOrderByIdAsc";
        final String expected = 
                "select e from Simple e " +
                "order by e.id asc";
        
        // when
        String result = QueryRoot.create(name, "Simple").getJpqlQuery().trim();
        
        // then
        assertEquals(expected, result);
    }
    
}
