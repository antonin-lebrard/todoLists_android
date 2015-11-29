package com.perso.antonleb.projetandroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.perso.antonleb.projetandroid.async.LoadUserTask;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.server.AntoninServerClient;
import com.perso.antonleb.projetandroid.server.INoteServerClient;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class NoteService extends Service
{
    protected final INoteServiceBinder binder = new NoteServiceBinder(this);
    protected final INoteServerClient client = new AntoninServerClient();

    public LoadUserTask loadUserTask()
    {
        LoadUserTask exec = new LoadUserTask(client);
        return exec;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
