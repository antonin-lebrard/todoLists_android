package com.perso.antonleb.projetandroid.services;

import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public abstract class AbstractServiceConnection<T extends IBinder> implements ServiceConnection
{
    protected List<ServiceConnectionListener<T>> connectionListeners;

    public AbstractServiceConnection()
    {
        this.connectionListeners = new ArrayList<>();
    }

    protected void notifyConnection(T binder)
    {
        for(ServiceConnectionListener<T> listener : this.connectionListeners)
            listener.onServiceConnected(binder);
    }

    public void then(ServiceConnectionListener<T> listener)
    {
        this.connectionListeners.add(listener);
    }
}
