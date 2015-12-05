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
public class LoadUserTask extends AsyncTask<UserKey, ActionProgress<UserKey>, Collection<IUser>>
{
    protected final INoteDBClient client;
    protected final List<UserLoadingListener> listeners;

    public LoadUserTask(INoteDBClient client, UserLoadingListener ... listener)
    {
        super();
        this.client = client;
        this.listeners = new ArrayList<>(Arrays.asList(listener));
    }

    @Override
    protected Collection<IUser> doInBackground(UserKey... params) {
        Collection<IUser> users = new LinkedList<>();

        for(UserKey key : params) {
            ActionProgress<UserKey> result;
            try {
                IUser loaded = client.getUser(key);
                users.add(loaded);
                result = new ActionProgress<UserKey>(key, ActionResult.SUCCESS);
            } catch (DBRequestException e) {
                e.printStackTrace();
                result = new ActionProgress<UserKey>(key, ActionResult.FAIL);
            }
            this.publishProgress(result);
        }

        return users;
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
    protected void onPostExecute(Collection<IUser> users)
    {
        for(IUser user : users) {
            for(UserLoadingListener listener : this.listeners) {
                if(listener != null) {
                    listener.onUserLoaded(user);
                }
            }
        }
    }
}
