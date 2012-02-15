package com.ctp.cdi.query.meta.extractor;

import com.ctp.cdi.query.meta.DaoEntity;

public interface MetadataExtractor {

    /**
     * Read entity meta data for a class.
     * @return          Meta data packed in a {@link DaoEntity}, or {@code null}
     *                  if the extractor was not able to find data.
     */
    DaoEntity extract(Class<?> daoClass);

}
