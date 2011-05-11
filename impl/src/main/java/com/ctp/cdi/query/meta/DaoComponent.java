package com.ctp.cdi.query.meta;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.reflection.AnnotationInstanceProvider;

import com.ctp.cdi.query.WithEntityManager;

/**
 * Stores information about a specific DAO. Extracts information about:
 * <ul>
 *     <li>The DAO class</li>
 *     <li>The target entity the DAO is for</li>
 *     <li>The primary key class</li>
 *     <li>All methods of the DAO.</li>
 * </ul>
 * 
 * @author thomashug
 */
public class DaoComponent {
    
    private static final Logger LOG = Logger.getLogger(DaoComponent.class);

    private Class<?> daoClass;
    private DaoEntity entityClass;
    private Annotation[] qualifiers;
    
    private Map<Method, DaoMethod> methods = new HashMap<Method, DaoMethod>();
    
    public DaoComponent(Class<?> daoClass, DaoEntity entityClass) {
        if (entityClass == null) {
            throw new RuntimeException("Entity class cannot be null");
        }
        this.daoClass = daoClass;
        this.entityClass = entityClass;
        initialize();
    }
    
    /**
     * Looks up method meta data by a Method object.
     * @param method    The DAO method.
     * @return          Method meta data.
     */
    public DaoMethod lookupMethod(Method method) {
        return methods.get(method);
    }
    
    /**
     * Looks up the method type by a Method object.
     * @param method    The DAO method.
     * @return          Method meta data.
     */
    public MethodType lookupMethodType(Method method) {
        return lookupMethod(method).getMethodType();
    }

    /**
     * Gets the entity class related the DAO.
     * @return          The class of the entity related to the DAO.
     */
    public Class<?> getEntityClass() {
        return entityClass.getEntityClass();
    }

    /**
     * Gets the entity primary key class related the DAO.
     * @return          The class of the entity primary key related to the DAO.
     */
    public Class<? extends Serializable> getPrimaryKey() {
        return entityClass.getPrimaryClass();
    }
    
    /**
     * Returns the original DAO class this meta data is related to.
     * @return          The class of the DAO.
     */
    public Class<?> getDaoClass() {
        return daoClass;
    }

    /**
     * Returns qualifiers for selecting an entity manager for the DAO component.
     * @return          A list of annotations, empty when using the default entity manager.
     */
    public Annotation[] getEntityManagerQualifiers() {
        return qualifiers;
    }

    private void initialize() {
        Collection<Class<?>> allImplemented = collectClasses();
        for (Class<?> implemented : allImplemented) {
            Method[] daoClassMethods = implemented.getDeclaredMethods();
            for (Method daoClassMethod : daoClassMethods) {
                DaoMethod daoMethod = new DaoMethod(daoClassMethod, this);
                methods.put(daoClassMethod, daoMethod);
            }
        }
        if (daoClass.isAnnotationPresent(WithEntityManager.class)) {
            Class<? extends Annotation>[] annotations = daoClass.getAnnotation(WithEntityManager.class).value();
            qualifiers = new Annotation[annotations.length];
            AnnotationInstanceProvider provider = new AnnotationInstanceProvider();
            for (int i = 0; i < annotations.length; i++) {
                Class<? extends Annotation> clazz = annotations[i];
                qualifiers[i] = provider.get(clazz, new HashMap<String, Object>());
            }
        } else {
            qualifiers = new Annotation[] {};
        }
    }
    
    private Set<Class<?>> collectClasses() {
        Set<Class<?>> result = new HashSet<Class<?>>();
        Class<?> current = daoClass;
        while (!Object.class.equals(current) && current != null) {
            result.add(current);
            Class<?>[] interfaces = current.getInterfaces();
            if (interfaces != null) {
                result.addAll(Arrays.asList(interfaces));
            }
            current = current.getSuperclass();
        }
        LOG.debugv("collectClasses(): Found {0} for {1}", result, daoClass);
        return result;
    }

}
