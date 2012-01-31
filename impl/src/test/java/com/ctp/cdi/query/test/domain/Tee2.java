package com.ctp.cdi.query.test.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 *
 */
@IdClass(TeeId.class)
@Entity
public class Tee2 {
    @Column(nullable = false)
    @Id
    private long teeSetId;

    @Column(nullable = false)
    @Id
    private long holeId;

    public long getTeeSetId() {
        return teeSetId;
    }

    public void setTeeSetId(long teeSetId) {
        this.teeSetId = teeSetId;
    }

    public long getHoleId() {
        return holeId;
    }

    public void setHoleId(long holeId) {
        this.holeId = holeId;
    }
}
