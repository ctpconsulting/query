package com.ctp.cdi.query.test.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TeeId implements Serializable {
    @Column(nullable = false)
    private long teeSetId;

    @Column(nullable = false)
    private long holeId;

    public TeeId() {
    }

    public TeeId(long teeSetId, long holeId) {
        this.teeSetId = teeSetId;
        this.holeId = holeId;
    }

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
