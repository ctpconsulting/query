package com.ctp.cdi.query.handler;

import static com.ctp.cdi.query.util.EntityUtils.entityName;
import static com.ctp.cdi.query.util.EntityUtils.isNew;
import static com.ctp.cdi.query.util.QueryUtils.isEmpty;
import static com.ctp.cdi.query.util.QueryUtils.isString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.SingularAttribute;

import org.jboss.solder.logging.Logger;
import org.jboss.solder.properties.Property;
import org.jboss.solder.properties.query.NamedPropertyCriteria;
import org.jboss.solder.properties.query.PropertyQueries;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.spi.DelegateQueryHandler;
import com.ctp.cdi.query.spi.QueryInvocationContext;

/**
 * Implement basic functionality from the {@link EntityDao}.
 *
 * @author thomashug
 *
 * @param <E>   Entity type.
 * @param <PK>  Primary key type, must be a serializable.
 */
public class EntityDaoHandler<E, PK extends Serializable> extends AbstractEntityDao<E, PK>
        implements DelegateQueryHandler {

    private final Logger log = Logger.getLogger(EntityDaoHandler.class);
    
    @Inject
    private QueryInvocationContext context;

    @Override
    public E save(E entity) {
        if (isNew(entity)) {
            entityManager().persist(entity);
            return entity;
        }
        return entityManager().merge(entity);
    }

    @Override
    public E saveAndFlush(E entity) {
        E result = save(entity);
        entityManager().flush();
        return result;
    }

    @Override
    public void refresh(E entity) {
        entityManager().refresh(entity);
    }

    @Override
    public E findBy(PK primaryKey) {
        return entityManager().find(entityClass(), primaryKey);
    }

    @Override
    public List<E> findBy(E example, SingularAttribute<E, ?>... attributes) {
        return findBy(example, -1, -1, attributes);
    }

    @Override
    public List<E> findBy(E example, int start, int max, SingularAttribute<E, ?>... attributes) {
        return executeExampleQuery(example,start,max,false,attributes);
    }

    @Override
    public List<E> findByLike(E example, SingularAttribute<E, ?>... attributes) {
        return findByLike(example, -1, -1, attributes);
    }

    @Override
    public List<E> findByLike(E example, int start, int max, SingularAttribute<E, ?>... attributes) {
        return executeExampleQuery(example,start,max,true,attributes);
    }

    @Override
    public List<E> findAll() {
        return entityManager().createQuery(allQuery(), entityClass()).getResultList();
    }

    @Override
    public List<E> findAll(int start, int max) {
        TypedQuery<E> query = entityManager().createQuery(allQuery(), entityClass());
        if (start > 0) {
            query.setFirstResult(start);
        }
        if (max > 0) {
            query.setMaxResults(max);
        }
        return query.getResultList();
    }

    @Override
    public Long count() {
        return entityManager().createQuery(countQuery(), Long.class).getSingleResult();
    }

    @Override
    public Long count(E example, SingularAttribute<E, ?>... attributes) {
        return executeCountQuery(example,false,attributes);
    }

    @Override
    public Long countLike(E example, SingularAttribute<E, ?>... attributes) {
        return executeCountQuery(example,true,attributes);
    }

    @Override
    public void remove(E entity) {
        entityManager().remove(entity);
    }

    @Override
    public void flush() {
        entityManager().flush();
    }

    @Override
    public EntityManager entityManager() {
        return context.getEntityManager();
    }

    @Override
    public CriteriaQuery<E> criteriaQuery() {
        return entityManager().getCriteriaBuilder().createQuery(entityClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<E> entityClass() {
        return (Class<E>) context.getEntityClass();
    }

    // ----------------------------------------------------------------------------
    // PRIVATE
    // ----------------------------------------------------------------------------

    private String allQuery() {
        return QueryBuilder.selectQuery(entityName(entityClass()));
    }

    private String countQuery() {
        return QueryBuilder.countQuery(entityName(entityClass()));
    }

    private String exampleQuery(String queryBase, List<Property<Object>> properties, boolean useLikeOperator) {
        StringBuilder jpqlQuery = new StringBuilder(queryBase).append(" where ");
        jpqlQuery.append(prepareWhere(properties, useLikeOperator));
        return jpqlQuery.toString();
    }

    private void addParameters(TypedQuery<?> query, E example, List<Property<Object>> properties, boolean useLikeOperator) {
        for (Property<Object> property : properties) {
            property.setAccessible();
            query.setParameter(property.getName(), transform(property.getValue(example), useLikeOperator));
        }
    }

    private Object transform(Object value, final boolean useLikeOperator) {
        if (value != null && useLikeOperator && isString(value)) {
            // seems to be an OpenJPA bug:
            // parameters in querys fail validation, e.g. UPPER(e.name) like UPPER(:name) 
            String result = ((String) value).toUpperCase();
            return "%" + result + "%";
        }
        return value;
    }

    private String prepareWhere(List<Property<Object>> properties, boolean useLikeOperator) {
        Iterator<Property<Object>> iterator = properties.iterator();
        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            Property<Object> property = iterator.next();
            String name = property.getName();
            if (useLikeOperator && property.getJavaClass().getName().equals(String.class.getName())) {
                result.append("UPPER(e.").append(name).append(") like :").append(name)
                        .append(iterator.hasNext() ? " and " : "");
            } else {
                result.append("e.").append(name).append(" = :").append(name).append(iterator.hasNext() ? " and " : "");
            }
        }
        return result.toString();
    }

    private List<String> extractPropertyNames(SingularAttribute<E, ?>... attributes) {
        List<String> result = new ArrayList<String>(attributes.length);
        for (SingularAttribute<E, ?> attribute : attributes) {
            result.add(attribute.getName());
        }
        return result;
    }
    
    private  List<Property<Object>> extractProperties(SingularAttribute<E, ?>... attributes) {
        List<String> names = extractPropertyNames(attributes);
        List<Property<Object>> properties = PropertyQueries.createQuery(entityClass())
                .addCriteria(new NamedPropertyCriteria(names.toArray(new String[] {}))).getResultList();
        return properties;
    }
    
    private List<E> executeExampleQuery(E example, int start, int max, boolean useLikeOperator, SingularAttribute<E, ?>... attributes) {
        //Not sure if this should be the intended behaviour
        //when we don't get any attributes maybe we should
        //return a empty list instead of all results
        if (isEmpty(attributes)) {
            return findAll(start, max);
        }

        List<Property<Object>> properties = extractProperties(attributes);
        String jpqlQuery = exampleQuery(allQuery(), properties, useLikeOperator);
        log.debugv("findBy|findByLike: Created query {0}", jpqlQuery);
        TypedQuery<E> query = entityManager().createQuery(jpqlQuery, entityClass());

        // set starting position
        if (start > 0) {
            query.setFirstResult(start);
        }

        // set maximum results
        if (max > 0) {
            query.setMaxResults(max);
        }

        addParameters(query, example, properties, useLikeOperator);
        return query.getResultList();
    }
    
    private Long executeCountQuery(E example, boolean useLikeOperator, SingularAttribute<E, ?>... attributes) {
        if (isEmpty(attributes)) {
            return count();
        }
        List<Property<Object>> properties = extractProperties(attributes);
        String jpqlQuery = exampleQuery(countQuery(), properties, useLikeOperator);
        log.debugv("count: Created query {0}", jpqlQuery);
        TypedQuery<Long> query = entityManager().createQuery(jpqlQuery, Long.class);
        addParameters(query, example, properties, useLikeOperator);
        return query.getSingleResult();
    }

}
