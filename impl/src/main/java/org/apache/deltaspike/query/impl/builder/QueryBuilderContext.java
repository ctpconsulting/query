package org.apache.deltaspike.query.impl.builder;

/**
 *
 * @author thomashug
 */
public class QueryBuilderContext {

    private final StringBuilder builder;
    private int counter = 1;
    
    public QueryBuilderContext() {
        this.builder = new StringBuilder();
    }
    
    public int increment() {
        return counter++;
    }
    
    public QueryBuilderContext append(String string) {
        builder.append(string);
        return this;
    }
    
    public String resultString() {
        return builder.toString();
    }

    public int getCounter() {
        return counter;
    }

}
