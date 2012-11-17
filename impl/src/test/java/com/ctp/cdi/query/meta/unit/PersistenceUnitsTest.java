package com.ctp.cdi.query.meta.unit;

import static com.ctp.cdi.query.test.util.TestDeployments.TEST_FILTER;
import static com.ctp.cdi.query.test.util.TestDeployments.addDependencies;
import static com.ctp.cdi.query.test.util.TestDeployments.createApiArchive;
import static com.ctp.cdi.query.test.util.TestDeployments.createImplPackages;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ctp.cdi.query.QueryExtension;
import com.ctp.cdi.query.meta.DaoEntity;
import com.ctp.cdi.query.test.TransactionalTestCase;
import com.ctp.cdi.query.test.domain.Parent;
import com.ctp.cdi.query.test.domain.TeeId;
import com.ctp.cdi.query.test.domain.mapped.MappedOne;
import com.ctp.cdi.query.test.domain.mapped.MappedThree;
import com.ctp.cdi.query.test.domain.mapped.MappedTwo;
import com.ctp.cdi.query.test.service.MappedOneDao;
import com.ctp.cdi.query.test.util.Logging;

@RunWith(Arquillian.class)
public class PersistenceUnitsTest {

    @Deployment
    public static Archive<?> deployment() {
        Logging.reconfigure();
        return addDependencies(ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsLibrary(createApiArchive())
                .addPackages(true, TEST_FILTER, createImplPackages())
                .addPackages(true, Parent.class.getPackage())
                .addClasses(QueryExtension.class, TransactionalTestCase.class, MappedOneDao.class)
                .addAsWebInfResource("test-mapped-persistence.xml", ArchivePaths.create("classes/META-INF/persistence.xml"))
                .addAsWebInfResource("test-default-orm.xml", ArchivePaths.create("classes/META-INF/orm.xml"))
                .addAsWebInfResource("test-custom-orm.xml", ArchivePaths.create("classes/META-INF/custom-orm.xml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("META-INF/services/javax.enterprise.inject.spi.Extension", 
                        ArchivePaths.create("classes/META-INF/services/javax.enterprise.inject.spi.Extension")));
    }
    
    @Test
    public void should_recognize_entity_data() {
        // given
  
        // when
        boolean positive1 = PersistenceUnits.instance().isEntity(MappedOne.class);
        boolean positive2 = PersistenceUnits.instance().isEntity(MappedTwo.class);
        boolean positive3 = PersistenceUnits.instance().isEntity(MappedThree.class);
        boolean negative = PersistenceUnits.instance().isEntity(Long.class);
        
        // then
        assertTrue(positive1);
        assertTrue(positive2);
        assertTrue(positive3);
        assertFalse(negative);
    }

    @Test
    public void should_recognize_ids() {
        // given
        
        // when
        String idField1 = PersistenceUnits.instance().primaryKeyField(MappedOne.class);
        String idField2 = PersistenceUnits.instance().primaryKeyField(MappedThree.class);
        
        // then
        assertEquals("id", idField1);
        assertEquals("id", idField2);
    }

    @Test
    public void should_recognize_name() {
        // given
        
        // when
        String name = PersistenceUnits.instance().entityName(MappedOne.class);
        
        // then
        assertEquals("Mapped_One", name);
    }

    @Test
    public void should_recognize_id_class() {
        // given
        
        // when
        Class<?> idClass = PersistenceUnits.instance().primaryKeyIdClass(MappedTwo.class);
        
        // then
        assertEquals(TeeId.class, idClass);
    }

    @Test
    public void should_prepare_dao_entity() {
        // given
        
        // when
        DaoEntity entity1 = PersistenceUnits.instance().lookupMetadata(MappedOne.class);
        DaoEntity entity2 = PersistenceUnits.instance().lookupMetadata(MappedTwo.class);
        DaoEntity entity3 = PersistenceUnits.instance().lookupMetadata(MappedThree.class);
        
        // then
        assertNotNull(entity1);
        assertNotNull(entity2);
        assertEquals(Long.class, entity1.getPrimaryClass());
        assertEquals(TeeId.class, entity2.getPrimaryClass());
        assertEquals(Long.class, entity3.getPrimaryClass());
    }

}
