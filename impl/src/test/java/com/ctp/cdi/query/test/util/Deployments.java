package com.ctp.cdi.query.test.util;

import org.jboss.seam.solder.serviceHandler.QueryExtension;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.ctp.cdi.query.Dao;

public abstract class Deployments {
    
    private static final String EXTENSION = "services/javax.enterprise.inject.spi.Extension";
    private static final String EXT_CLASS = QueryExtension.class.getName().toString();

    public static JavaArchive initDeployment() {
	Logging.quiet();
	return ShrinkWrap.create(JavaArchive.class, "test.jar")
		.addPackage(Dao.class.getPackage())
		.addPackage(QueryExtension.class.getPackage())
		.addAsManifestResource(new ByteArrayAsset(EXT_CLASS.getBytes()),EXTENSION)
		.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
}
