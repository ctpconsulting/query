package com.ctp.cdi.query;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;

@Dao
public interface EntityDao<E, PK> {

    /**
     * Persist (new entity) or merge the given entity.
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
     * Convenience access to {@link EntityManager#remove(Object)}.
     * @param entity            Entity to remove.
     */
    public void remove(E entity);
    
    /**
     * Convenience access to {@link EntityManager#refresh(Object)}.
     * @param entity            Entity to refresh.
     */
    public void refresh(E entity);

    /**
     * Convenience access to {@link EntityManager#flush()}.
     */
    public void flush();

    /**
     * Entity lookup by primary key. Convenicence method around {@link EntityManager#find(Class, Object)}.
     * @param primaryKey        DB primary key.
     * @return                  Entity identified by primary or null if it does not exist.
     */
    public E findBy(PK primaryKey);

    /**
     * Lookup all existing entities of entity class {@code <E>}.
     * @return                  List of entities, empty if none found.
     */
    public List<E> findAll();
    
    /**
     * Query by example - for a given object and a specific set of properties.
     * TODO: Can we put something like that in a producer and have queries restricted by example?
     * TODO: As far as I remember Hibernate does this by all non-null properties.
     * @param example           Sample entity. Query all like.
     * @param attributes        Which attributes to consider for the query.
     * @return                  List of entities matching the example, or empty if none found.
     */
    public List<E> findBy(E example, SingularAttribute<E, ?>... attributes);

    /**
     * Count all existing entities of entity class {@code <E>}.
     * @return                  Counter.
     */
    public Long count();

}
