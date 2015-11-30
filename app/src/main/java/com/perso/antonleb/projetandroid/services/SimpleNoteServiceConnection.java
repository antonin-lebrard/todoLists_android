package com.perso.antonleb.projetandroid.services;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class SimpleNoteServiceConnection implements ServiceConnection
{
    protected INoteServiceBinder service;
    protected final INoteConsumer consumer;

    public SimpleNoteServiceConnection(INoteConsumer consumer)
    {
        this.consumer = consumer;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        if(service instanceof INoteServiceBinder) {
            this.service = INoteServiceBinder.class.cast(service);
            this.service.addConsumer(this.consumer);
            this.consumer.onBinderCreated();
        }
        else {
            throw new IllegalArgumentException("Binded service is not an instance of INoteServiceBinder.");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        this.consumer.unbind(this.service);
        this.service = null;
    }

    public INoteServiceBinder getService()
    {
        return service;
    }
}
