package org.apache.deltaspike.query.impl.meta;

public class NonEntityException extends RuntimeException {

    private static final long serialVersionUID = 7196572996969471161L;

    public NonEntityException() {
        super();
    }

    public NonEntityException(String message) {
        super(message);
    }

    public NonEntityException(Throwable cause) {
        super(cause);
    }

    public NonEntityException(String message, Throwable cause) {
        super(message, cause);
    }

}
