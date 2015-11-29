package com.perso.antonleb.projetandroid.services;

import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.consumable.IConsumer;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Un objet (comme une activitée) qui capture les résultats des méthodes d'un INoteService.
 */
public interface INoteConsumer extends IConsumer
{
    public void onUserLoaded(IUser user);
}
