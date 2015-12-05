package com.perso.antonleb.projetandroid.exceptions;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class DBRequestException extends Exception
{
    public DBRequestException()
    { }

    public DBRequestException(String detailMessage)
    {
        super(detailMessage);
    }

    public DBRequestException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public DBRequestException(Throwable throwable)
    {
        super(throwable);
    }
}
