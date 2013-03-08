/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.deltaspike.query.impl;

import java.lang.reflect.InvocationHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.impl.meta.DaoComponentsFactory;
import org.apache.deltaspike.query.impl.meta.unit.PersistenceUnits;

/**
 * The main extension class for CDI queries, based on Seam Solder service handlers. Overrides the behavior for looking
 * up handler classes.
 *
 * @author thomashug
 */
public class QueryExtension implements Extension
{

    private static final Logger log = Logger.getLogger(QueryExtension.class.getName());

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery before)
    {
        PersistenceUnits.instance().init();
    }

    <X> void processAnnotatedType(@Observes ProcessAnnotatedType<X> event, BeanManager beanManager)
    {
        if (isDao(event.getAnnotatedType()))
        {
            log.log(Level.FINER, "getHandlerClass: Dao annotation detected on {0}", event.getAnnotatedType());
            boolean added = DaoComponentsFactory.instance().add(event.getAnnotatedType().getJavaClass());
            if (!added)
            {
                log.log(Level.INFO, "getHandlerClass: Type {0} ignored as it's not related to an entity",
                        event.getAnnotatedType());
            }
        }
    }

    private <X> boolean isDao(AnnotatedType<X> annotatedType)
    {
        return (annotatedType.isAnnotationPresent(Dao.class) ||
                annotatedType.getJavaClass().isAnnotationPresent(Dao.class)) &&
                !InvocationHandler.class.isAssignableFrom(annotatedType.getJavaClass());
    }

}
