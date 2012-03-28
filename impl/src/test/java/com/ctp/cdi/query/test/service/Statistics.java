package com.ctp.cdi.query.test.service;

public class Statistics {
 
    private final Double average;
    private final Long count;
    
    public Statistics(Double average, Long count) {
        this.average = average;
        this.count = count;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Statistics [")
                .append("average=").append(average)
                .append(", count=").append(count)
                .append("]");
        return builder.toString();
    }

    public Double getAverage() {
        return average;
    }

    public Long getCount() {
        return count;
    }
}
