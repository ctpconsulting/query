package org.apache.deltaspike.query.test.util;

import java.net.URL;

import org.apache.deltaspike.query.api.AbstractEntityDao;
import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.api.EntityDao;
import org.apache.deltaspike.query.api.EntityManagerDao;
import org.apache.deltaspike.query.api.FirstResult;
import org.apache.deltaspike.query.api.MaxResults;
import org.apache.deltaspike.query.api.Modifying;
import org.apache.deltaspike.query.api.NonEntity;
import org.apache.deltaspike.query.api.Query;
import org.apache.deltaspike.query.api.QueryParam;
import org.apache.deltaspike.query.api.QueryResult;
import org.apache.deltaspike.query.api.WithEntityManager;
import org.apache.deltaspike.query.api.audit.CreatedOn;
import org.apache.deltaspike.query.api.audit.CurrentUser;
import org.apache.deltaspike.query.api.audit.ModifiedBy;
import org.apache.deltaspike.query.api.audit.ModifiedOn;
import org.apache.deltaspike.query.api.criteria.Criteria;
import org.apache.deltaspike.query.api.criteria.CriteriaSupport;
import org.apache.deltaspike.query.api.criteria.QuerySelection;
import org.apache.deltaspike.query.impl.QueryExtension;
import org.apache.deltaspike.query.impl.audit.AuditEntityListener;
import org.apache.deltaspike.query.impl.builder.QueryBuilder;
import org.apache.deltaspike.query.impl.criteria.QueryCriteria;
import org.apache.deltaspike.query.impl.handler.QueryHandler;
import org.apache.deltaspike.query.impl.meta.DaoComponents;
import org.apache.deltaspike.query.impl.param.Parameters;
import org.apache.deltaspike.query.impl.property.Property;
import org.apache.deltaspike.query.impl.util.EntityUtils;
import org.apache.deltaspike.query.spi.DelegateQueryHandler;
import org.apache.deltaspike.query.spi.QueryInvocationContext;
import org.apache.deltaspike.query.test.TransactionalTestCase;
import org.apache.deltaspike.query.test.domain.AuditedEntity;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.filter.ExcludeRegExpPaths;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;


public abstract class TestDeployments {

    public static Filter<ArchivePath> TEST_FILTER = new ExcludeRegExpPaths(".*Test.*class");

    public static MavenDependencyResolver resolver() {
        return DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
    }

    public static WebArchive initDeployment() {
        return initDeployment(".*test.*");
    }

    /**
     * Create a basic deployment containing API classes, the Extension class and
     * test persistence / beans descriptor.
     *
     * @return Basic web archive.
     */
    public static WebArchive initDeployment(String testFilter) {
        Logging.reconfigure();
        WebArchive archive = ShrinkWrap
            .create(WebArchive.class, "test.war")
            .addAsLibrary(createApiArchive())
            .addClasses(QueryExtension.class)
            .addClasses(TransactionalTestCase.class)
            .addPackages(true, TEST_FILTER, createImplPackages())
            .addPackages(true, AuditedEntity.class.getPackage())
            .addPackages(true, new ExcludeRegExpPaths(testFilter), TransactionalTestCase.class.getPackage())
            .addAsWebInfResource(classpathResource("test-persistence.xml", "META-INF/persistence.xml"),
                    ArchivePaths.create("classes/META-INF/persistence.xml"))
            .addAsWebInfResource("META-INF/services/javax.enterprise.inject.spi.Extension",
                    ArchivePaths.create("classes/META-INF/services/javax.enterprise.inject.spi.Extension"))
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

        return addDependencies(archive);
    }

    public static Package[] createImplPackages() {
        return new Package[] {
                AuditEntityListener.class.getPackage(),
                QueryBuilder.class.getPackage(),
                QueryCriteria.class.getPackage(),
                QueryHandler.class.getPackage(),
                DaoComponents.class.getPackage(),
                Parameters.class.getPackage(),
                EntityUtils.class.getPackage(),
                Property.class.getPackage()
        };
    }

    public static Archive<?> createApiArchive() {
        return ShrinkWrap.create(JavaArchive.class, "archive.jar")
                .addClasses(AbstractEntityDao.class, Dao.class, EntityDao.class,
                        FirstResult.class, MaxResults.class, Modifying.class,
                        NonEntity.class, Query.class, QueryParam.class, QueryResult.class,
                        WithEntityManager.class, EntityManagerDao.class)
                .addClasses(Criteria.class, QuerySelection.class, CriteriaSupport.class)
                .addClasses(CreatedOn.class, CurrentUser.class, ModifiedBy.class, ModifiedOn.class)
                .addClasses(DelegateQueryHandler.class, QueryInvocationContext.class);
    }

    public static WebArchive addDependencies(WebArchive archive) {
        return archive.addAsLibraries(resolver()
                .artifact("org.apache.deltaspike.core:deltaspike-core-api")
                .artifact("org.apache.deltaspike.core:deltaspike-core-impl")
                .resolveAsFiles());
    }

    public static String classpathResource(String resource, String fallback) {
        URL url = TestDeployments.class.getClassLoader().getResource(resource);
        return url != null ? resource : fallback;
    }

}
