package com.ctp.cdi.query.meta.unit;

import static junit.framework.Assert.assertEquals;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.ctp.cdi.query.QueryExtension;
import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.meta.DaoComponents;
import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.mapped.MappedOne;
import com.ctp.cdi.query.test.service.MappedOneDao;

public class OrmXmlBasedDaoTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(QueryExtension.class)
                .addPackage(DaoComponents.class.getPackage())
                .addPackage(QueryBuilder.class.getPackage())
                .addPackage(MappedOne.class.getPackage())
                .addClasses(MappedOneDao.class)
                .addAsWebInfResource("test-mapped-persistence.xml", ArchivePaths.create("classes/META-INF/persistence.xml"))
                .addAsWebInfResource("test-default-orm.xml", ArchivePaths.create("classes/META-INF/orm.xml"))
                .addAsWebInfResource("test-custom-orm.xml", ArchivePaths.create("classes/META-INF/custom-orm.xml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("glassfish-resources.xml");
    }
    
    @Produces
    @PersistenceContext
    private EntityManager entityManager;
    
    @Inject
    private MappedOneDao mappedOneDao;
    
    @Test
    public void shouldFindBy() {
        // given
        MappedOne one = createMappedOne("shouldFindBy");
        
        // when
        MappedOne byPk = mappedOneDao.findBy(one.getId());
        MappedOne byName = mappedOneDao.findByName("shouldFindBy");
        
        // then
        assertEquals(one.getId(), byPk.getId());
        assertEquals(one.getId(), byName.getId());
    }

    private MappedOne createMappedOne(String name) {
        MappedOne result = new MappedOne(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }
    
}
