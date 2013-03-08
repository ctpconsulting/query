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
package org.apache.deltaspike.query.impl.meta.extractor;

import org.apache.deltaspike.query.api.Dao;
import org.apache.deltaspike.query.api.NonEntity;
import org.apache.deltaspike.query.impl.meta.DaoEntity;
import org.apache.deltaspike.query.impl.meta.verifier.EntityVerifier;
import org.apache.deltaspike.query.impl.meta.verifier.Verifier;
import org.apache.deltaspike.query.impl.util.EntityUtils;

public class AnnotationMetadataExtractor implements MetadataExtractor
{

    private final Verifier<Class<?>> verifier;

    public AnnotationMetadataExtractor()
    {
        this.verifier = new EntityVerifier();
    }

    @Override
    public DaoEntity extract(Class<?> daoClass)
    {
        Dao dao = daoClass.getAnnotation(Dao.class);
        Class<?> daoEntity = dao.value();
        boolean isEntityClass = !NonEntity.class.equals(daoEntity) && verifier.verify(daoEntity);
        if (isEntityClass)
        {
            return new DaoEntity(daoEntity, EntityUtils.primaryKeyClass(daoEntity));
        }
        return null;
    }

}
