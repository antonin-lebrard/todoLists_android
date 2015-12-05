package com.perso.antonleb.projetandroid.services;

import android.os.IBinder;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public interface ServiceConnectionListener<T extends IBinder>
{
    public void onServiceConnected(T binded);
}
