package com.ctp.cdi.query.test.service;

public interface MyEntityDao<E> {

    E saveAndFlushAndRefresh(E entity);

}
