package com.ctp.cdi.query.audit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.AuditedEntity;
import com.ctp.cdi.query.test.domain.Principal;
import com.ctp.cdi.query.test.util.TestDeployments;

@RunWith(Arquillian.class)
public class AuditEntityListenerTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deploymentWithLibs() {
        return TestDeployments.initDeployment()
                .addPackage(AuditEntityListener.class.getPackage())
                .addAsWebInfResource("test-orm.xml", ArchivePaths.create("classes/META-INF/orm.xml"))
                .addPackage(AuditedEntity.class.getPackage());
    }
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final String who = "test999";
    
    @Produces @CurrentUser
    public String who() {
        return who;
    }
    
    @Produces @CurrentUser
    public Principal entity() {
        Principal p = new Principal(who);
        entityManager.persist(p);
        return p;
    }
    
    @Test
    public void shouldSetCreationDate() throws Exception {
        // given
        AuditedEntity entity = new AuditedEntity();
        
        // when
        entityManager.persist(entity);
        entityManager.flush();
        
        // then
        assertNotNull(entity.getCreated());
        assertNotNull(entity.getModified());
        assertEquals(entity.getCreated().getTime(), entity.getModified());
    }
    
    @Test
    public void shouldSetModificationDate() throws Exception {
        // given
        AuditedEntity entity = new AuditedEntity();
        entityManager.persist(entity);
        entityManager.flush();
        
        // when
        entity = entityManager.merge(entity);
        entity.setName("test");
        entityManager.flush();
        
        // then
        assertNotNull(entity.getGregorianModified());
        assertNotNull(entity.getSqlModified());
        assertNotNull(entity.getTimestamp());
    }
    
    @Test
    public void shouldSetChangingPrincipal() {
        // given
        AuditedEntity entity = new AuditedEntity();
        
        // when
        entityManager.persist(entity);
        entityManager.flush();
        
        // then
        assertNotNull(entity.getChanger());
        assertEquals(who, entity.getChanger());
        assertNotNull(entity.getPrincipal());
        assertEquals(who, entity.getPrincipal().getName());
    }

}
