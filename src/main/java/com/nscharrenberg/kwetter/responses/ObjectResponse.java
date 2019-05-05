package com.nscharrenberg.kwetter.responses;

public class ObjectResponse<T> {
    private int code;
    private String message = null;
    private T object;

    public ObjectResponse(int code, String message, T object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }

    public ObjectResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
