package com.ctp.cdi.query.builder.part;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author thomashug
 */
public class QueryRootTest {
    
    @Test
    public void shouldCreateQuery() {
        // given
        final String name = "findByNameAndCreatedBetweenOrNullableIsNull";
        final String expected = 
                "select e from Simple e " +
                "where e.name = ?1 " +
                "and e.created between ?2 and ?3 " +
                "or e.nullable IS NULL";
        
        // when
        String result = QueryRoot.create(name, "Simple").createJpql();
        
        // then
        System.out.println(result);
        Assert.assertEquals(expected, result);
    }
    
}
