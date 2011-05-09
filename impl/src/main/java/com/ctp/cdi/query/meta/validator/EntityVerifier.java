package com.ctp.cdi.query.meta.validator;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jboss.seam.solder.properties.query.AnnotatedPropertyCriteria;
import org.jboss.seam.solder.properties.query.PropertyQueries;
import org.jboss.seam.solder.properties.query.PropertyQuery;

public class EntityVerifier implements Verifier<Class<?>>{

	// TODO what about xml descriptors?
	@Override
	public boolean verify(Class<?> entity) {
		return isEntity(entity) && hasPrimaryKey(entity);
	}

	boolean isEntity(Class<?> entity) {
		return entity.isAnnotationPresent(Entity.class);
	}

	boolean hasPrimaryKey(Class<?> entity) {
		final AnnotatedPropertyCriteria hasId = new AnnotatedPropertyCriteria(Id.class);
		PropertyQuery<Serializable> query = PropertyQueries.<Serializable>createQuery(entity)
	      												   .addCriteria(hasId);
		return query.getFirstResult() != null;
	}
	
}
