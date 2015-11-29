package com.perso.antonleb.projetandroid.services;

import android.os.IBinder;

import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.consumable.IConsumable;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public interface INoteServiceBinder extends IBinder, IConsumable<INoteConsumer>
{
    public void loadUser(UserKey user);
}
