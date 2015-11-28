package com.perso.antonleb.projetandroid.exceptions;

/**
 * Created by CEDRIC on 28/11/2015.
 */
public class CategoryAlreadyExistException extends Exception {
    public CategoryAlreadyExistException()
    { }

    public CategoryAlreadyExistException(String detailMessage)
    {
        super(detailMessage);
    }

    public CategoryAlreadyExistException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public CategoryAlreadyExistException(Throwable throwable)
    {
        super(throwable);
    }
}
