package com.ctp.cdi.query.meta.verifier;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import javax.persistence.Entity;

import org.junit.Test;

import com.ctp.cdi.query.test.domain.Simple;

public class EntityVerifierTest {

    @Test
    public void shouldAcceptEntityClass() {
        // given
        EntityVerifier entityVerifier = new EntityVerifier();

        // when
        boolean isValid = entityVerifier.isEntity(Simple.class);

        // then
        assertTrue(isValid);
    }

    @Test
    public void shouldHavePrimaryKeyAnnotated() {
        // given
        EntityVerifier entityVerifier = new EntityVerifier();

        // when
        boolean isValid = entityVerifier.hasPrimaryKey(Simple.class);

        // then
        assertTrue(isValid);
    }

    @Test
    public void shouldAcceptEntityClassWithIdKey() {
        // given
        EntityVerifier entityVerifier = new EntityVerifier();

        // when
        boolean isValid = entityVerifier.verify(Simple.class);

        // then
        assertTrue(isValid);
    }

    @Test
    public void shouldNotAcceptClassWithEntityAnnotationButWithoutIdField() {
        // given
        EntityVerifier entityVerifier = new EntityVerifier();

        // when
        boolean isValid = entityVerifier.verify(EntityWithoutId.class);

        // then
        assertFalse(isValid);

    }

    @Entity
    private static class EntityWithoutId {

    }

}
