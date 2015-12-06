package com.perso.antonleb.projetandroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.perso.antonleb.projetandroid.async.CloseDBServiceTask;
import com.perso.antonleb.projetandroid.async.CloseClientTask;
import com.perso.antonleb.projetandroid.async.LaunchNoteCommandTask;
import com.perso.antonleb.projetandroid.async.LoadUserTask;
import com.perso.antonleb.projetandroid.async.OpenClientTask;
import com.perso.antonleb.projetandroid.async.command.AddCategoryCommand;
import com.perso.antonleb.projetandroid.async.command.AddNoteCommand;
import com.perso.antonleb.projetandroid.async.command.ICommand;
import com.perso.antonleb.projetandroid.async.command.ICommandQueue;
import com.perso.antonleb.projetandroid.async.command.ListCommandQueue;
import com.perso.antonleb.projetandroid.async.command.RemoveNoteCommand;
import com.perso.antonleb.projetandroid.datas.CategoryKey;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.datas.creators.SimplePolymorphCreator;
import com.perso.antonleb.projetandroid.exceptions.DBRequestException;
import com.perso.antonleb.projetandroid.listeners.CommandResultListener;
import com.perso.antonleb.projetandroid.listeners.NetworkStateListener;
import com.perso.antonleb.projetandroid.listeners.UserLoadingListener;
import com.perso.antonleb.projetandroid.receiver.NetworkStateReceiver;
import com.perso.antonleb.projetandroid.server.AntoninServerDBClient;
import com.perso.antonleb.projetandroid.server.INoteDBClient;
import com.perso.antonleb.projetandroid.server.LocalDBClient;
import com.perso.antonleb.projetandroid.utils.ParcelableUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public final class NoteDBService extends Service implements NetworkStateListener, UserLoadingListener, CommandResultListener
{
    protected final NoteDBServiceBinder binder;
    protected NetworkStateReceiver networkStatut;

    protected final INoteDBClient onlineClient;
    protected final INoteDBClient offlineClient;

    protected ICommandQueue commandQueue;
    protected AsyncTask<?,?,?> runningCommand;

    protected boolean destroyed;
    protected List<LoadUserTask> waiting;

    public NoteDBService()
    {
        super();
        this.binder = new NoteDBServiceBinder(this);
        this.onlineClient = new AntoninServerDBClient();
        this.offlineClient = new LocalDBClient(this);
        this.networkStatut = null;

        this.commandQueue = new ListCommandQueue();
        this.runningCommand = null;
        this.destroyed = false;
        this.waiting = new ArrayList<>();
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
        LoadUserTask loadUserTask = new LoadUserTask(getClient(), key, listener, this);

        if(networkStatut.isOffline()) {
            loadUserTask.execute();
        }
        else if(this.commandQueue.size() != 0) {
            this.waiting.add(loadUserTask);
        }
        else {
            loadUserTask.execute();
        }
    }

    public void addNote(CategoryKey key, String note)
    {
        AddNoteCommand command = new AddNoteCommand(key, note);
        this.commandQueue.push(command);
        this.executeNextCommand();

        LaunchNoteCommandTask commandTask = new LaunchNoteCommandTask(this.offlineClient);
        commandTask.execute(command);
    }

    public void removeNote(CategoryKey key, String note)
    {
        RemoveNoteCommand command = new RemoveNoteCommand(key, note);
        this.commandQueue.push(command);
        this.executeNextCommand();

        LaunchNoteCommandTask commandTask = new LaunchNoteCommandTask(this.offlineClient);
        commandTask.execute(command);
    }

    public void addCategory(CategoryKey key)
    {
        AddCategoryCommand command = new AddCategoryCommand(key);
        this.commandQueue.push(command);
        this.executeNextCommand();

        LaunchNoteCommandTask commandTask = new LaunchNoteCommandTask(this.offlineClient);
        commandTask.execute(command);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.networkStatut = NetworkStateReceiver.createFor(this, this);

        new OpenClientTask(this.onlineClient).execute();
        new OpenClientTask(this.offlineClient).execute();

        // Plus simple qu'avec un thread.
        File file = new File(this.getFilesDir(), "COMMANDS_DB");

        if (file.exists()) {
            SimplePolymorphCreator<ICommandQueue> creator = SimplePolymorphCreator.getCreator(ICommandQueue.class);
            Parcel parcel = null;
            try {
                parcel = ParcelableUtils.toParcel(this, file);
                this.setCommandQueue(creator.createFromParcel(parcel));
                parcel.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onEnteringOnlineMode() {
        this.executeNextCommand();
    }

    @Override
    public void onEnteringOfflineMode() {
        if(this.runningCommand != null) {
            this.runningCommand.cancel(true);
            this.runningCommand = null;
        }

        while(this.waiting.size() > 0) {
            LoadUserTask task = this.waiting.remove(0);
            task.setClient(offlineClient);
            task.execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.destroyed = true;
        if(this.runningCommand != null) {
            this.runningCommand.cancel(true);
            this.runningCommand = null;
        }

        new CloseClientTask(this.onlineClient).execute();
        new CloseClientTask(this.offlineClient).execute();
        new CloseDBServiceTask(this).execute();

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
    public void onUserLoadingFail(UserKey key)
    {

    }

    @Override
    public void onUserLoadingSuccess(UserKey key)
    {

    }

    @Override
    public void onCommandFail(ICommand command) {
        this.runningCommand = null;
        this.executeNextCommand();
    }

    @Override
    public void onCommandSuccess(ICommand command) {
        this.commandQueue.release();
        this.runningCommand = null;

        if(this.commandQueue.size() <= 0) {
            while(this.waiting.size() > 0) {
                LoadUserTask task = this.waiting.remove(0);
                task.setClient(getClient());
                task.execute();
            }
        }

        this.executeNextCommand();
    }

    protected void executeNextCommand()
    {
        if(!destroyed && this.networkStatut.isOnline() && this.runningCommand == null && this.commandQueue.hasNext()) {
            Log.i(getClass().getCanonicalName(), "LAUNCHING COMMAND...");
            LaunchNoteCommandTask task = new LaunchNoteCommandTask(this.onlineClient, this);
            this.runningCommand = task;
            task.execute(this.commandQueue.next());
        }
    }

    public ICommandQueue getCommandQueue()
    {
        return commandQueue;
    }

    public void setCommandQueue(ICommandQueue commandQueue)
    {
        if(this.runningCommand != null) {
            this.runningCommand.cancel(true);
            this.runningCommand = null;
        }

        while(this.commandQueue.hasNext()) {
            commandQueue.push(this.commandQueue.next());
        }

        this.commandQueue = commandQueue;
        this.executeNextCommand();
    }
}
