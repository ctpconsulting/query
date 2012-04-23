package com.ctp.cdi.query.handler;

import static com.ctp.cdi.query.util.QueryUtils.isEmpty;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.SingularAttribute;

import org.jboss.solder.logging.Logger;
import org.jboss.solder.properties.Property;
import org.jboss.solder.properties.query.NamedPropertyCriteria;
import org.jboss.solder.properties.query.PropertyQueries;

import com.ctp.cdi.query.AbstractEntityDao;
import com.ctp.cdi.query.EntityDao;
import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.criteria.Criteria;
import com.ctp.cdi.query.criteria.QueryCriteria;
import com.ctp.cdi.query.criteria.QuerySelection;
import com.ctp.cdi.query.criteria.selection.AttributeQuerySelection;
import com.ctp.cdi.query.criteria.selection.numeric.Abs;
import com.ctp.cdi.query.criteria.selection.numeric.Avg;
import com.ctp.cdi.query.criteria.selection.numeric.Count;
import com.ctp.cdi.query.criteria.selection.numeric.Max;
import com.ctp.cdi.query.criteria.selection.numeric.Min;
import com.ctp.cdi.query.criteria.selection.numeric.Modulo;
import com.ctp.cdi.query.criteria.selection.numeric.Neg;
import com.ctp.cdi.query.criteria.selection.numeric.Sum;
import com.ctp.cdi.query.criteria.selection.strings.Lower;
import com.ctp.cdi.query.criteria.selection.strings.SubstringFrom;
import com.ctp.cdi.query.criteria.selection.strings.SubstringFromTo;
import com.ctp.cdi.query.criteria.selection.strings.Upper;
import com.ctp.cdi.query.criteria.selection.temporal.CurrentDate;
import com.ctp.cdi.query.criteria.selection.temporal.CurrentTime;
import com.ctp.cdi.query.criteria.selection.temporal.CurrentTimestamp;
import com.ctp.cdi.query.util.EntityUtils;

/**
 * Implement basic functionality from the {@link EntityDao}.
 *
 * @author thomashug
 *
 * @param <E>   Entity type.
 * @param <PK>  Primary key type, must be a serializable.
 */
public class EntityDaoHandler<E, PK extends Serializable> extends AbstractEntityDao<E, PK> {

    private final Logger log = Logger.getLogger(EntityDaoHandler.class);

    private final EntityManager entityManager;
    private final Class<E> entityClass;
    private final String entityName;

    public EntityDaoHandler(EntityManager entityManager, Class<E> entityClass) {
        if (null == entityManager) {
            throw new IllegalStateException("EntityManager cannot be null.");
        }
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.entityName = EntityUtils.entityName(entityClass);
    }

    public static boolean contains(Method method) {
        return extract(method) != null;
    }

    public static <E, PK extends Serializable> EntityDaoHandler<E, PK> create(EntityManager e, Class<E> entityClass) {
        return new EntityDaoHandler<E, PK>(e, entityClass);
    }

    public Object invoke(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return extract(method).invoke(this, args);
    }

    @Override
    public E save(E entity) {
        if (EntityUtils.isNew(entity)) {
            entityManager.persist(entity);
            return entity;
        }
        return entityManager.merge(entity);
    }

    @Override
    public E saveAndFlush(E entity) {
        E result = save(entity);
        entityManager.flush();
        return result;
    }

    @Override
    public void refresh(E entity) {
        entityManager.refresh(entity);
    }

    @Override
    public E findBy(PK primaryKey) {
        return entityManager.find(entityClass, primaryKey);
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
        return entityManager.createQuery(allQuery(), entityClass).getResultList();
    }

    @Override
    public List<E> findAll(int start, int max) {
        TypedQuery<E> query = entityManager.createQuery(allQuery(),entityClass);
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
        return entityManager.createQuery(countQuery(), Long.class).getSingleResult();
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
        entityManager.remove(entity);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public CriteriaQuery<E> criteriaQuery() {
        return entityManager.getCriteriaBuilder().createQuery(entityClass);
    }

    @Override
    public Class<E> entityClass() {
        return entityClass;
    }
    
    @Override
    public Criteria<E, E> criteria() {
        return new QueryCriteria<E, E>(entityClass, entityClass, entityManager);
    }
    
    @Override
    public <T> Criteria<T, T> where(Class<T> clazz) {
        return new QueryCriteria<T, T>(clazz, clazz, entityManager);
    }
    
    @Override
    public <T> Criteria<T, T> where(Class<T> clazz, JoinType joinType) {
        return new QueryCriteria<T, T>(clazz, clazz, getEntityManager(), joinType);
    }
    
    @Override
    public <X> QuerySelection<E, X> attribute(SingularAttribute<E, X> attribute) {
        return new AttributeQuerySelection<E, X>(attribute);
    }
    
    // ----------------------------------------------------------------------------
    // NUMERIC QUERY SELECTION
    // ----------------------------------------------------------------------------
    
    @Override
    public <N extends Number> QuerySelection<E, N> abs(SingularAttribute<E, N> attribute) {
        return new Abs<E, N>(attribute);
    }
    
    @Override
    public <N extends Number> QuerySelection<E, N> avg(SingularAttribute<E, N> attribute) {
        return new Avg<E, N>(attribute);
    }
    
    @Override
    public <N extends Number> QuerySelection<E, N> count(SingularAttribute<E, N> attribute) {
        return new Count<E, N>(attribute);
    }

    @Override
    public <N extends Number> QuerySelection<E, N> max(SingularAttribute<E, N> attribute) {
        return new Max<E, N>(attribute);
    }

    @Override
    public <N extends Number> QuerySelection<E, N> min(SingularAttribute<E, N> attribute) {
        return new Min<E, N>(attribute);
    }

    @Override
    public <N extends Number> QuerySelection<E, N> neg(SingularAttribute<E, N> attribute) {
        return new Neg<E, N>(attribute);
    }

    @Override
    public <N extends Number> QuerySelection<E, N> sum(SingularAttribute<E, N> attribute) {
        return new Sum<E, N>(attribute);
    }
    
    @Override
    public QuerySelection<E, Integer> modulo(SingularAttribute<E, Integer> attribute, Integer modulo) {
        return new Modulo<E>(attribute, modulo);
    }
    
    // ----------------------------------------------------------------------------
    // STRING QUERY SELECTION
    // ----------------------------------------------------------------------------
    
    @Override
    public QuerySelection<E, String> upper(SingularAttribute<E, String> attribute) {
        return new Upper<E>(attribute);
    }

    @Override
    public QuerySelection<E, String> lower(SingularAttribute<E, String> attribute) {
        return new Lower<E>(attribute);
    }
    
    @Override
    public QuerySelection<E, String> substring(SingularAttribute<E, String> attribute, int from) {
        return new SubstringFrom<E>(attribute, from);
    }

    @Override
    public QuerySelection<E, String> substring(SingularAttribute<E, String> attribute, int from, int length) {
        return new SubstringFromTo<E>(attribute, from, length);
    }
    
    // ----------------------------------------------------------------------------
    // TEMPORAL QUERY SELECTION
    // ----------------------------------------------------------------------------
    
    @Override
    public QuerySelection<E, Date> currDate() {
        return new CurrentDate<E>();
    }
    
    @Override
    public QuerySelection<E, Time> currTime() {
        return new CurrentTime<E>();
    }
    
    @Override
    public QuerySelection<E, Timestamp> currTStamp() {
        return new CurrentTimestamp<E>();
    }
    
    // ----------------------------------------------------------------------------
    // PRIVATE
    // ----------------------------------------------------------------------------

    private static Method extract(Method method) {
        try {
            String name = method.getName();
            return EntityDaoHandler.class.getMethod(name, method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String allQuery() {
        return QueryBuilder.selectQuery(entityName);
    }

    private String countQuery() {
        return QueryBuilder.countQuery(entityName);
    }

    private String exampleQuery(String queryBase, List<Property<Object>> properties, boolean useLikeOperator) {
        StringBuilder jpqlQuery = new StringBuilder(queryBase).append(" where ");
        jpqlQuery.append(prepareWhere(properties, useLikeOperator));
        return jpqlQuery.toString();
    }

    private void addParameters(TypedQuery<?> query, E example, List<Property<Object>> properties) {
        for (Property<Object> property : properties) {
            property.setAccessible();
            query.setParameter(property.getName(), property.getValue(example));
        }
    }

    private String prepareWhere(List<Property<Object>> properties, boolean useLikeOperator) {
        Iterator<Property<Object>> iterator = properties.iterator();
        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            Property<Object> property = iterator.next();
            String name = property.getName();
            if(useLikeOperator && property.getJavaClass().getName().equals(String.class.getName())) {
                result.append("UPPER(e.").append(name).append(") like UPPER(concat('%',:").append(name).append(iterator.hasNext() ? ",'%')) and " : ",'%'))");
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
        List<Property<Object>> properties = PropertyQueries.createQuery(entityClass)
                .addCriteria(new NamedPropertyCriteria(names.toArray(new String[] {}))).getResultList();
        return properties;
    }
    
    private List<E> executeExampleQuery(E example, int start, int max, boolean useLikeOperator, SingularAttribute...attributes) {
        //Not sure if this should be the intended behaviour
        //when we don't get any attributes maybe we should
        //return a empty list instead of all results
        if (isEmpty(attributes)) {
            return findAll(start, max);
        }

        List<Property<Object>> properties = extractProperties(attributes);
        String jpqlQuery = exampleQuery(allQuery(),properties,useLikeOperator);
        log.debugv("findBy|findByLike: Created query {0}", jpqlQuery);
        TypedQuery<E> query = entityManager.createQuery(jpqlQuery, entityClass);

        // set starting position
        if (start > 0) {
            query.setFirstResult(start);
        }

        // set maximum results
        if (max > 0) {
            query.setMaxResults(max);
        }

        addParameters(query, example, properties);
        return query.getResultList();
    }
    
    private Long executeCountQuery(E example, boolean useLikeOperator, SingularAttribute...attributes) {
        if (isEmpty(attributes)) {
            return count();
        }
        List<Property<Object>> properties = extractProperties(attributes);
        String jpqlQuery = exampleQuery(countQuery(),properties,useLikeOperator);
        log.debugv("count: Created query {0}", jpqlQuery);
        TypedQuery<Long> query = entityManager.createQuery(jpqlQuery, Long.class);
        addParameters(query, example, properties);
        return query.getSingleResult();
    }

}
