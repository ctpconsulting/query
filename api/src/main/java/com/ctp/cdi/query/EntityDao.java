package com.ctp.cdi.query;

import java.util.List;

public interface EntityDao<E, PK> {

    /**
     * Persist or merge the given entity.
     * @param entity            Entity to save.
     * @return                  Returns the modified entity.
     */
    public E save(E entity);

    /**
     * {@link #save(Object)}s the given entity and flushes the persistence context afterwards.
     * @param entity            Entity to save.
     * @return                  Returns the modified entity.
     */
    public E saveAndFlush(E entity);
    
    /**
     * Convenience access to entity manager remove - remove the given entity.
     * @param entity            Entity to remove.
     */
    public void remove(E entity);
    
    /**
     * Convenience access to entity manager refresh - refreshes the given entity.
     * @param entity            Entity to refresh.
     */
    public void refresh(E entity);

    /**
     * Convenience access to entity manager flush.
     */
    public void flush();

    public E findBy(PK primaryKey);

    public List<E> findAll();

    public Long count();

    public boolean exists(PK primaryKey);

}
