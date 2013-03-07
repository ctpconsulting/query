package org.apache.deltaspike.query.test.service;

public interface MyEntityDao<E> {

    E saveAndFlushAndRefresh(E entity);

}
