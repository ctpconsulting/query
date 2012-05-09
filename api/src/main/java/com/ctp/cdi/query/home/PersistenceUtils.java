package com.ctp.cdi.query.home;

import java.io.Serializable;

interface PersistenceUtils<E, PK> extends Serializable {

    Class<E> entityClass(Class<?> homeClass);
    
    PK primaryKeyValue(E entity);

}
