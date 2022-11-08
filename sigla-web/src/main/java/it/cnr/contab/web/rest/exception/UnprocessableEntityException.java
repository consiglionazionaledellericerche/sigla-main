package it.cnr.contab.web.rest.exception;

import java.io.Serializable;

public class UnprocessableEntityException  extends RuntimeException{
    private final Serializable entity;

    public Serializable getEntity() {
        return entity;
    }

    public UnprocessableEntityException(Serializable entity) {
        this.entity = entity;
    }

    public UnprocessableEntityException(String s, Serializable entity) {
        super(s);
        this.entity = entity;
    }

    public UnprocessableEntityException(String s, Throwable throwable, Serializable entity) {
        super(s, throwable);
        this.entity = entity;
    }

    public UnprocessableEntityException(Throwable throwable, Serializable entity) {
        super(throwable);
        this.entity = entity;
    }

    public UnprocessableEntityException(String s, Throwable throwable, boolean b, boolean b1, Serializable entity) {
        super(s, throwable, b, b1);
        this.entity = entity;
    }

}
