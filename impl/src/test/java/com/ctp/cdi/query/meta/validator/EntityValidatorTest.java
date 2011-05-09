package com.ctp.cdi.query.meta.validator;

import javax.persistence.Entity;

import org.junit.Assert;
import org.junit.Test;

import com.ctp.cdi.query.test.domain.Simple;

public class EntityValidatorTest {

	@Test
	public void shouldAcceptEntityClass() {
		// given
		EntityVerifier entityVerifier = new EntityVerifier();
		
		// when
		boolean isValid = entityVerifier.isEntity(Simple.class);
		
		// then
		Assert.assertTrue(isValid);
	}

	@Test
	public void shouldHavePrimaryKeyAnnotated() {
		// given
		EntityVerifier entityVerifier = new EntityVerifier();

		// when
		boolean isValid = entityVerifier.hasPrimaryKey(Simple.class);

		// then 
		Assert.assertTrue(isValid);
	}
	
	@Test
    public void shouldAcceptEntityClassWithIdKey() {
	    // given
	    EntityVerifier entityVerifier = new EntityVerifier();

	    // when
	    boolean isValid = entityVerifier.verify(Simple.class);

	    // then 
	    Assert.assertTrue(isValid);
    }
	
	@Test
    public void shouldNotAcceptClassWithEntityAnnotationButWithoutIdField() {
	    // given
	    EntityVerifier entityVerifier = new EntityVerifier();

	    // when
	    boolean isValid = entityVerifier.verify(EntityWithoutId.class);

	    // then 
	    Assert.assertFalse(isValid);
	    
    }
	
	@Entity
	private static class EntityWithoutId {
		
	}
	
}
