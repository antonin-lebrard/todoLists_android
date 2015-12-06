package com.perso.antonleb.projetandroid.listeners;

import com.perso.antonleb.projetandroid.server.INoteDBClient;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public interface ClientOpenListener
{
    public void onOpenSuccess(INoteDBClient opened);
    public void onOpenFail(INoteDBClient unopened);
}
