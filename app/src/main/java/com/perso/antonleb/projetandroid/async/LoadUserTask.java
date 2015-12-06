package com.perso.antonleb.projetandroid.async;

import android.os.AsyncTask;
import android.util.Log;

import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.exceptions.DBRequestException;
import com.perso.antonleb.projetandroid.listeners.UserLoadingListener;
import com.perso.antonleb.projetandroid.server.INoteDBClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Charge des utilisateurs.
 */
public class LoadUserTask extends AsyncTask<Void, ActionProgress<UserKey>, IUser>
{
    protected INoteDBClient client;
    protected final List<UserLoadingListener> listeners;
    protected final UserKey key;

    public LoadUserTask(INoteDBClient client, UserKey key, UserLoadingListener ... listener)
    {
        super();
        this.client = client;
        this.listeners = new ArrayList<>(Arrays.asList(listener));
        this.key = key;
    }

    @Override
    protected IUser doInBackground(Void... params) {
        IUser user = null;

        ActionProgress<UserKey> result;
        try {
            user = client.getUser(key);
            result = new ActionProgress<UserKey>(key, ActionResult.SUCCESS);
        } catch (DBRequestException e) {
            e.printStackTrace();
            result = new ActionProgress<UserKey>(key, ActionResult.FAIL);
        }
        this.publishProgress(result);

        return user;
    }

    @Override
    protected void onProgressUpdate(ActionProgress<UserKey>... values)
    {
        if(values != null) {
            for (ActionProgress<UserKey> progress : values) {
                for(UserLoadingListener listener : this.listeners) {
                    if(listener != null) {
                        switch (progress.result) {
                            case FAIL:
                                listener.onUserLoadingFail(progress.data);
                                break;
                            case SUCCESS:
                                listener.onUserLoadingSuccess(progress.data);
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onPostExecute(IUser user)
    {
        for(UserLoadingListener listener : this.listeners) {
            if(listener != null) {
                listener.onUserLoaded(user);
            }
        }
    }

    public INoteDBClient getClient() {
        return client;
    }

    public void setClient(INoteDBClient client) {
        this.client = client;
    }
}
