package com.ctp.cdi.query.meta.verifier;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import com.ctp.cdi.query.test.domain.Simple;

public class EntityVerifierTest {

    @Test
    public void should_accept_entity_class() {
        // given
        EntityVerifier entityVerifier = new EntityVerifier();

        // when
        boolean isValid = entityVerifier.verify(Simple.class);

        // then
        assertTrue(isValid);
    }

    @Test
    public void should_not_accept_class_without_entity_annotation() {
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
