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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.deltaspike.query.impl.meta.DaoEntity;
import org.apache.deltaspike.query.test.domain.Simple;
import org.apache.deltaspike.query.test.service.DaoInterface;
import org.apache.deltaspike.query.test.service.SimpleDao;
import org.junit.Test;

public class TypeMetadataExtractorTest
{

    @Test
    public void should_extract_from_class()
    {
        // given
        MetadataExtractor extractor = new TypeMetadataExtractor();

        // when
        DaoEntity result = extractor.extract(SimpleDao.class);

        // then
        assertNotNull(result);
        assertEquals(Simple.class, result.getEntityClass());
        assertEquals(Long.class, result.getPrimaryClass());
    }

    @Test
    public void should_not_extract_from_annotation()
    {
        // given
        MetadataExtractor extractor = new TypeMetadataExtractor();

        // when
        DaoEntity result = extractor.extract(DaoInterface.class);

        // then
        assertNull(result);
    }

}
