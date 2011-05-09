package com.ctp.cdi.query.meta.validator;

public interface Verifier<T> {

	boolean verify(T t);

}
