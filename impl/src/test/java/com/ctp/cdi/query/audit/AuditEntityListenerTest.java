package com.ctp.cdi.query.audit;

import static com.ctp.cdi.query.test.util.TestDeployments.initDeployment;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.junit.Test;

import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.AuditedEntity;
import com.ctp.cdi.query.test.domain.Principal;

public class AuditEntityListenerTest extends TransactionalTestCase {
    
    @Deployment
    public static Archive<?> deployment() {
        return initDeployment()
                .addPackage(AuditEntityListener.class.getPackage())
                .addAsWebInfResource("test-orm.xml", ArchivePaths.create("classes/META-INF/orm.xml"))
                .addPackage(AuditedEntity.class.getPackage());
    }
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final String who = "test999";
    private Principal principal = new Principal(who);
    
    @Produces @CurrentUser
    public String who() {
        return who;
    }
    
    @Produces @CurrentUser
    public Principal entity() throws Exception {
        try {
            entityManager.persist(principal);
        } catch (Throwable e) {
        }
        return principal;
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
        entity = entityManager.find(AuditedEntity.class, entity.getId());
        entity.setName("test");
        entityManager.flush();
        
        // then
        assertNotNull(entity.getGregorianModified());
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

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
