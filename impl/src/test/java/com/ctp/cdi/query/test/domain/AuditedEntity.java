package com.ctp.cdi.query.test.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ctp.cdi.query.audit.ModifiedBy;
import com.ctp.cdi.query.audit.CreatedOn;
import com.ctp.cdi.query.audit.ModifiedOn;

@Entity
@SuppressWarnings("serial")
public class AuditedEntity implements Serializable {

    @Id @GeneratedValue
    private Long id;
    
    @Temporal(TemporalType.TIME)
    @CreatedOn
    private Calendar created;
    
    private String name;
    
    @ModifiedBy
    private String changer;
    
    @ModifiedBy
    private Principal principal;
    
    @Temporal(TemporalType.TIME)
    @ModifiedOn(onCreate = true)
    private java.util.Date modified;
    
    private java.sql.Date sqlModified;
    
    @Temporal(TemporalType.TIME)
    @ModifiedOn
    private GregorianCalendar gregorianModified;
    
    @ModifiedOn
    private Timestamp timestamp;
    
    public AuditedEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @ModifiedOn
    public java.sql.Date getSqlModified() {
        return sqlModified;
    }

    public void setSqlModified(java.sql.Date sqlModified) {
        this.sqlModified = sqlModified;
    }

    public Calendar getCreated() {
        return created;
    }

    public java.util.Date getModified() {
        return modified;
    }

    public GregorianCalendar getGregorianModified() {
        return gregorianModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getChanger() {
        return changer;
    }

    public void setChanger(String changer) {
        this.changer = changer;
    }

    public Principal getPrincipal() {
        return principal;
    }
}
