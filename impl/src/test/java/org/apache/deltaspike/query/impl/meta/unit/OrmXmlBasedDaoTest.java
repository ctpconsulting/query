package org.apache.deltaspike.query.impl.meta.unit;

import static org.junit.Assert.assertEquals;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.deltaspike.query.test.TransactionalTestCase;
import org.apache.deltaspike.query.test.domain.mapped.MappedOne;
import org.apache.deltaspike.query.test.domain.mapped.MappedThree;
import org.apache.deltaspike.query.test.domain.mapped.MappedTwo;
import org.apache.deltaspike.query.test.service.MappedOneDao;
import org.apache.deltaspike.query.test.util.TestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;


public class OrmXmlBasedDaoTest extends TransactionalTestCase {

    @Deployment
    public static Archive<?> deployment() {
        return TestDeployments.initDeployment("(.*mapped.*)|(.*test.*)")
                .addClasses(MappedOneDao.class)
                .addAsLibraries(
                        ShrinkWrap.create(JavaArchive.class, "domain.jar")
                            .addClasses(MappedOne.class, MappedTwo.class, MappedThree.class)
                            .addAsResource("test-custom-orm.xml", ArchivePaths.create("META-INF/custom-orm.xml"))
                 )
                .addAsWebInfResource("test-mapped-persistence.xml", ArchivePaths.create("classes/META-INF/persistence.xml"))
                .addAsWebInfResource("test-default-orm.xml", ArchivePaths.create("classes/META-INF/orm.xml"));
    }

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private MappedOneDao mappedOneDao;

    @Test
    public void should_find_by() {
        // given
        MappedOne one = createMappedOne("shouldFindBy");

        // when
        MappedOne byPk = mappedOneDao.findBy(one.getId());
        MappedOne byName = mappedOneDao.findByName("shouldFindBy");

        // then
        assertEquals(one.getId(), byPk.getId());
        assertEquals(one.getId(), byName.getId());
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    private MappedOne createMappedOne(String name) {
        MappedOne result = new MappedOne(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
