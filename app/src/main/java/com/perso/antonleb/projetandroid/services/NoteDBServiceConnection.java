package com.perso.antonleb.projetandroid.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Permet de connecter une activitée à un service de type NoteDB.
 */
public final class NoteDBServiceConnection extends AbstractServiceConnection<NoteDBServiceBinder>
{
    protected NoteDBServiceBinder service;

    public static NoteDBServiceConnection connect(Context context)
    {
        NoteDBServiceConnection result = new NoteDBServiceConnection();
        Intent intent = new Intent(context, NoteDBService.class);
        context.bindService(intent, result, Context.BIND_AUTO_CREATE);
        return result;
    }

    protected NoteDBServiceConnection()
    { }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        if(service instanceof NoteDBServiceBinder) {
            this.service = NoteDBServiceBinder.class.cast(service);
            this.notifyConnection(this.service);
        }
        else {
            throw new IllegalArgumentException("Binded service is not an instance of NoteDBServiceBinder.");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        this.service = null;
    }

    public NoteDBServiceBinder getService()
    {
        return service;
    }
}
