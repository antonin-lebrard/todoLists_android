package com.perso.antonleb.projetandroid.listeners;

import com.perso.antonleb.projetandroid.server.INoteDBClient;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public interface ClientCloseListener
{
    public void onCloseSuccess(INoteDBClient closed);
    public void onCloseFail(INoteDBClient unclosed);
}
