package com.perso.antonleb.projetandroid.exceptions;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class CommandExecutionException extends Exception
{
    public CommandExecutionException() {
        super();
    }

    public CommandExecutionException(String detailMessage) {
        super(detailMessage);
    }

    public CommandExecutionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CommandExecutionException(Throwable throwable) {
        super(throwable);
    }
}
