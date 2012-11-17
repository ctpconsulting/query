package com.ctp.cdi.query.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ctp.cdi.query.test.domain.Simple;
import com.ctp.cdi.query.test.service.SimpleDaoWithEntityManager;
import com.ctp.cdi.query.test.service.SimpleDaoWithOverriddenEntityManager;
import com.ctp.cdi.query.test.service.Simplistic;
import com.ctp.cdi.query.test.util.TestDeployments;

@RunWith(Arquillian.class)
public class EntityManagerTest {

    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment()
                .addClasses(SimpleDaoWithEntityManager.class,
                            SimpleDaoWithOverriddenEntityManager.class,
                            EntityManagerTestProducer.class,
                            Simplistic.class);
    }

    @Inject
    private SimpleDaoWithEntityManager daoWithAnnotation;

    @Inject
    private SimpleDaoWithOverriddenEntityManager daoWithInjection;

    @Test
    public void should_use_qualified_entity_manager() {
        // when
        List<Simple> result = daoWithAnnotation.findByName("testUseQualifiedEntityManager");

        // then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void should_use_injected_entity_manager() {
        // when
        List<Simple> result = daoWithInjection.findByName("testUseInjectedEntityManager");

        // then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void should_inject_entity_manager() {
        // when
        List<Simple> result = daoWithInjection.findWithEm("testInjectEntityManager");

        // then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}
