package com.ctp.cdi.query.home;

public class EntityMessage {

    private final MessageType messageType;
    private final HomeOperation operation;
    private final Object entity;
    private final Exception exception;

    public static EntityMessage created(Object entity) {
        return new EntityMessage(entity, MessageType.SUCCESS, HomeOperation.CREATE);
    }
    
    public static EntityMessage updated(Object entity) {
        return new EntityMessage(entity, MessageType.SUCCESS, HomeOperation.UPDATE);
    }
    
    public static EntityMessage deleted(Object entity) {
        return new EntityMessage(entity, MessageType.SUCCESS, HomeOperation.DELETE);
    }
    
    public static EntityMessage failed(Object entity, HomeOperation operation, Exception e) {
        return new EntityMessage(entity, MessageType.FAILED, operation, e);
    }
    
    private EntityMessage(Object entity, MessageType messageType, HomeOperation operation) {
        this(entity, messageType, operation, null);
    }
    
    private EntityMessage(Object entity, MessageType messageType, HomeOperation operation, Exception exception) {
        this.entity = entity;
        this.messageType = messageType;
        this.operation = operation;
        this.exception = exception;
    }
    
    public boolean isSuccess() {
        return MessageType.SUCCESS.equals(messageType);
    }
    
    public boolean isFailure() {
        return !isSuccess();
    }
    
    public HomeOperation getOperation() {
        return operation;
    }

    public Object getEntity() {
        return entity;
    }

    public Exception getException() {
        return exception;
    }
    
    public static enum HomeOperation {
        CREATE, UPDATE, DELETE
    }

    private static enum MessageType {
        SUCCESS, FAILED
    }

}
