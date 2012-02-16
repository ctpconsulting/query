package com.ctp.cdi.query.meta.verifier;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import com.ctp.cdi.query.test.domain.Simple;

public class EntityVerifierTest {

    @Test
    public void shouldAcceptEntityClass() {
        // given
        EntityVerifier entityVerifier = new EntityVerifier();

        // when
        boolean isValid = entityVerifier.verify(Simple.class);

        // then
        assertTrue(isValid);
    }

    @Test
    public void shouldNotAcceptClassWithoutEntityAnnotation() {
        // given
        EntityVerifier entityVerifier = new EntityVerifier();

        // when
        boolean isValid = entityVerifier.verify(EntityWithoutId.class);

        // then
        assertFalse(isValid);

    }

    private static class EntityWithoutId {

    }

}
