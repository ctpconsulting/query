package com.ctp.cdi.query.home;

import static com.ctp.cdi.query.home.EntityMessage.HomeOperation.CREATE;
import static com.ctp.cdi.query.home.EntityMessage.HomeOperation.DELETE;
import static com.ctp.cdi.query.home.EntityMessage.HomeOperation.UPDATE;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.EntityDao;

public abstract class EntityHome<E, PK extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private PK id;
    private E entity;
    private Class<E> entityClass;

    private @Inject NavigationProvider navigation;
    private @Inject ConversationProvider conversation;
    private @Inject Event<EntityMessage> event;
    private @Inject PersistenceUtils<E, PK> utils;

    private E search;
    private int page;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private long count;
    private List<E> pageItems;
    
    public Object create() {
        conversation.begin();
        return navigation.create();
    }
    
    public void retrieve() {
        if (navigation.isPostback()) {
            return;
        }
        if (conversation.isTransient()) {
            conversation.begin();
        }
        if (id == null) {
            entity = this.search;
        } else {
            entity = getEntityDao().findBy(getId());
        }
    }
    
    public Object update() {
        conversation.end();
        try {
            getEntityDao().save(entity);
            if (id == null) {
                event.fire(EntityMessage.created(entity));
                return navigation.search();
            } else {
                event.fire(EntityMessage.updated(entity));
                PK entityId = utils.primaryKeyValue(entity);
                return navigation.view(entityId);
            }
        } catch (Exception e) {
            event.fire(EntityMessage.failed(entity, id == null ? CREATE : UPDATE, e));
            return navigation.exception();
        }
    }
    
    public Object delete() {
        conversation.end();
        try {
            getEntityDao().remove(getEntityDao().findBy(getId()));
            getEntityDao().flush();
            return navigation.search();
        } catch (Exception e) {
            event.fire(EntityMessage.failed(entity, DELETE, e));
            return navigation.exception();
        }
    }
    
    public void search() {
        this.page = 0;
    }
    
    @SuppressWarnings("unchecked")
    public void paginate() {
        List<SingularAttribute<E, ?>> attributes = searchAttributes();
        if (attributes == null) {
            attributes = Collections.emptyList();
        }
        SingularAttribute<E, Object>[] attArray = attributes.toArray(new SingularAttribute[attributes.size()]);
        count = getEntityDao().countLike(search, attArray);
        pageItems = getEntityDao().findByLike(search, page * pageSize, pageSize, attArray);
    }
    
    protected abstract EntityDao<E, PK> getEntityDao();
    
    protected abstract List<SingularAttribute<E, ?>> searchAttributes();
    
    @PostConstruct
    protected void init() {
        try {
            entityClass = utils.entityClass(getClass());
            search = entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed initializing EntityHome", e);
        }
    }
    
    protected SingularAttributes<E> singularAttributes() {
        return new SingularAttributes<E>();
    }
    
    protected static class SingularAttributes<E> {
        
        private final List<SingularAttribute<E, ?>> attributes = new LinkedList<SingularAttribute<E,?>>();

        public SingularAttributes<E> add(Object value, SingularAttribute<E, ?> attribute) {
            attributes.add(attribute);
            return this;
        }
        
        public SingularAttributes<E> addIfNotEmpty(Object value, SingularAttribute<E, ?> attribute) {
            if (value instanceof String && isNotEmpty((String) value)) {
                attributes.add(attribute);
            } else if (value != null) {
                attributes.add(attribute);
            }
            return this;
        }
        
        private boolean isNotEmpty(String value) {
            return value != null && !"".equals(value);
        }
        
        public List<SingularAttribute<E, ?>> getAttributes() {
            return attributes;
        }
        
    }
    
    // ------------------------------------------------------------------------
    // ACCESSORS AND MUTATORS
    // ------------------------------------------------------------------------
    
    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public E getSearch() {
        return search;
    }

    public void setSearch(E search) {
        this.search = search;
    }

    public long getCount() {
        return count;
    }

    public List<E> getPageItems() {
        return pageItems;
    }

}
