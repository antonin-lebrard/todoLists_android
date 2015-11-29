package com.perso.antonleb.projetandroid.async;

import android.os.AsyncTask;

import com.perso.antonleb.projetandroid.consumable.ConsumableAsynTask;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.exceptions.ServerRequestException;
import com.perso.antonleb.projetandroid.server.INoteServerClient;
import com.perso.antonleb.projetandroid.services.INoteConsumer;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class LoadUserTask extends ConsumableAsynTask<UserKey, IUser, Collection<IUser>, INoteConsumer>
{
    protected final INoteServerClient client;

    public LoadUserTask(INoteServerClient client)
    {
        super();
        this.client = client;
    }

    @Override
    protected Collection<IUser> doInBackground(UserKey... params) {
        Collection<IUser> users = new LinkedList<IUser>();

        for(UserKey key : params) {
            try {
                IUser loaded = client.getUser(key.name);
                users.add(loaded);
                this.publishProgress(loaded);
            } catch (ServerRequestException e) {
                e.printStackTrace();
                users.add(null);
                this.publishProgress(null);
            }
        }

        return users;
    }

    @Override
    protected void onProgressUpdate(IUser... values)
    {
        if(values != null) {
            for (IUser user : values) {
                for (INoteConsumer consumer : this.consumers) {
                    consumer.onUserLoaded(user);
                }
            }
        }
        else {
            for (INoteConsumer consumer : this.consumers) {
                consumer.onUserLoaded(null);
            }
        }
    }

    @Override
    protected void onPostExecute(Collection<IUser> iUsers)
    { }
}
