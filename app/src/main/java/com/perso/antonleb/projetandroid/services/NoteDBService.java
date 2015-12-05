package com.perso.antonleb.projetandroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.perso.antonleb.projetandroid.async.LaunchNoteCommandTask;
import com.perso.antonleb.projetandroid.async.LoadUserTask;
import com.perso.antonleb.projetandroid.async.command.AddCategoryCommand;
import com.perso.antonleb.projetandroid.async.command.AddNoteCommand;
import com.perso.antonleb.projetandroid.async.command.ICommand;
import com.perso.antonleb.projetandroid.async.command.RemoveNoteCommand;
import com.perso.antonleb.projetandroid.datas.CategoryKey;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.exceptions.DBRequestException;
import com.perso.antonleb.projetandroid.listeners.CommandResultListener;
import com.perso.antonleb.projetandroid.listeners.NetworkStateListener;
import com.perso.antonleb.projetandroid.listeners.UserLoadingListener;
import com.perso.antonleb.projetandroid.receiver.NetworkStateReceiver;
import com.perso.antonleb.projetandroid.server.AntoninServerDBClient;
import com.perso.antonleb.projetandroid.server.INoteDBClient;
import com.perso.antonleb.projetandroid.server.LocalDBClient;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public final class NoteDBService extends Service implements NetworkStateListener, UserLoadingListener, CommandResultListener
{
    protected final NoteDBServiceBinder binder;
    protected NetworkStateReceiver networkStatut;

    protected final INoteDBClient onlineClient;
    protected final INoteDBClient offlineClient;

    public NoteDBService()
    {
        super();
        this.binder = new NoteDBServiceBinder(this);
        this.onlineClient = new AntoninServerDBClient();
        this.offlineClient = new LocalDBClient(this);
        this.networkStatut = null;
    }

    protected INoteDBClient getClient()
    {
        if(this.networkStatut.isOnline()) {
            return onlineClient;
        }
        else {
            return offlineClient;
        }
    }

    public void loadUser(UserKey key, UserLoadingListener listener)
    {
        LoadUserTask loadUserTask = new LoadUserTask(getClient(), listener, this);
        loadUserTask.execute(key);
    }

    public void addNote(CategoryKey key, String note, CommandResultListener listener)
    {
        AddNoteCommand command = new AddNoteCommand(key, note);
        LaunchNoteCommandTask commandTask = new LaunchNoteCommandTask(getClient(), listener, this);
        commandTask.execute(command);
    }

    public void removeNote(CategoryKey key, String note, CommandResultListener listener)
    {
        RemoveNoteCommand command = new RemoveNoteCommand(key, note);
        LaunchNoteCommandTask commandTask = new LaunchNoteCommandTask(getClient(), listener, this);
        commandTask.execute(command);
    }

    public void addCategory(CategoryKey key, CommandResultListener listener)
    {
        AddCategoryCommand command = new AddCategoryCommand(key);
        LaunchNoteCommandTask commandTask = new LaunchNoteCommandTask(getClient(), listener, this);
        commandTask.execute(command);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.networkStatut = NetworkStateReceiver.createFor(this, this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onEnteringOnlineMode() {

    }

    @Override
    public void onEnteringOfflineMode() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            this.onlineClient.close();
        } catch (DBRequestException e) {
            e.printStackTrace();
        }

        try {
            this.offlineClient.close();
        } catch (DBRequestException e) {
            e.printStackTrace();
        }

        this.unregisterReceiver(this.networkStatut);
    }

    @Override
    public void onUserLoaded(IUser user) {
        if(this.networkStatut.isOnline()) {
            try {
                this.offlineClient.setUser(user);
            } catch (DBRequestException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onUserLoadingFail(UserKey key) {

    }

    @Override
    public void onUserLoadingSuccess(UserKey key) {

    }

    @Override
    public void onCommandFail(ICommand command) {

    }

    @Override
    public void onCommandSuccess(ICommand command) {

    }
}
