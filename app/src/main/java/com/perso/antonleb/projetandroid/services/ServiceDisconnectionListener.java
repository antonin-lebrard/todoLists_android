package com.perso.antonleb.projetandroid.services;

import android.os.IBinder;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public interface ServiceDisconnectionListener<T extends IBinder>
{
    public void onServiceDisconnected(T binded);
}
