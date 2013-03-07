package org.apache.deltaspike.query.impl.meta.verifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.deltaspike.query.impl.meta.verifier.EntityVerifier;
import org.apache.deltaspike.query.test.domain.Simple;
import org.junit.Test;


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
