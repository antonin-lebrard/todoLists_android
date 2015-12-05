package com.perso.antonleb.projetandroid.listeners;

import com.perso.antonleb.projetandroid.async.command.ICommand;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public interface CommandResultListener
{
    public void onCommandFail(ICommand command);
    public void onCommandSuccess(ICommand command);
}
