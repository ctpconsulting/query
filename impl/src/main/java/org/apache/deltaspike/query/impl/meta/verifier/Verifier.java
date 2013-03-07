package org.apache.deltaspike.query.impl.meta.verifier;

public interface Verifier<T> {

    boolean verify(T t);

}
