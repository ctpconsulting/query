package com.ctp.cdi.query.test.util;

import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.ctp.cdi.query.Dao;
import com.ctp.cdi.query.QueryExtension;

public abstract class Deployments {

    /**
     * Create a basic deployment containing API classes, the Extension class
     * and test persistence / beans descriptor.
     * @return                  Basic web archive.
     */
    public static WebArchive initDeployment() {
	Logging.reconfigure();
	return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(Dao.class.getPackage())
                .addPackage(QueryExtension.class.getPackage())
                .addAsWebInfResource("test-persistence.xml", ArchivePaths.create("classes/META-INF/persistence.xml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsWebInfResource("glassfish-resources.xml");
	// TODO: Adding the datasource was somehow refusing to work with arquillian.xml.
	// Switched to web deployment for the time being.
    }

}
