package com.ctp.cdi.query.test.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ctp.cdi.query.audit.CreatedOn;
import com.ctp.cdi.query.audit.ModifiedBy;
import com.ctp.cdi.query.audit.ModifiedOn;

@Entity
@SuppressWarnings("serial")
public class AuditedEntity implements Serializable {

    @Id @GeneratedValue
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedOn
    private Calendar created;
    
    private String name;
    
    @ModifiedBy
    private String changer;
    
    @ModifiedBy
    @ManyToOne(targetEntity = Principal.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Principal principal;
    
    @Temporal(TemporalType.TIME)
    @ModifiedOn(onCreate = true)
    private java.util.Date modified;
    
    @Temporal(TemporalType.DATE)
    @ModifiedOn
    private Calendar gregorianModified;
    
    @ModifiedOn
    private Timestamp timestamp;
    
    public AuditedEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getCreated() {
        return created;
    }

    public java.util.Date getModified() {
        return modified;
    }

    public Calendar getGregorianModified() {
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
