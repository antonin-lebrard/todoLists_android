package com.perso.antonleb.projetandroid.exceptions;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class ServerRequestException extends Exception
{
    public ServerRequestException()
    { }

    public ServerRequestException(String detailMessage)
    {
        super(detailMessage);
    }

    public ServerRequestException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public ServerRequestException(Throwable throwable)
    {
        super(throwable);
    }
}
