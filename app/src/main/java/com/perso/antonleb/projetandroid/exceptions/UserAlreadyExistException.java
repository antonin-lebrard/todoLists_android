package com.perso.antonleb.projetandroid.exceptions;

/**
 * Created by CEDRIC on 28/11/2015.
 */
public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {
    }

    public UserAlreadyExistException(String detailMessage) {
        super(detailMessage);
    }

    public UserAlreadyExistException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UserAlreadyExistException(Throwable throwable) {
        super(throwable);
    }
}
