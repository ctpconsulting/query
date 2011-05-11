package com.ctp.cdi.query.meta.verifier;

public interface Verifier<T> {

    boolean verify(T t);

}
