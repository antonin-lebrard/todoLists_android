package com.perso.antonleb.projetandroid.async;

import android.os.AsyncTask;

import com.perso.antonleb.projetandroid.exceptions.DBRequestException;
import com.perso.antonleb.projetandroid.listeners.ClientCloseListener;
import com.perso.antonleb.projetandroid.server.INoteDBClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public final class CloseClientTask
        extends AsyncTask<Void, Void, ActionProgress<Void>>
{

    protected final INoteDBClient client;
    protected final List<ClientCloseListener> listeners;

    public CloseClientTask(INoteDBClient client, ClientCloseListener... listener)
    {
        super();
        this.client = client;
        this.listeners = new ArrayList<>(Arrays.asList(listener));
    }

    @Override
    protected void onPostExecute(ActionProgress<Void> result) {
        for (ClientCloseListener listener : this.listeners) {
            if(listener != null) {
                if(result.result == ActionResult.FAIL) {
                    listener.onCloseFail(this.client);
                }
                else {
                    listener.onCloseSuccess(this.client);
                }
            }
        }
    }

    @Override
    protected ActionProgress<Void> doInBackground(Void... voids) {
        ActionProgress<Void> result = null;

        try {
            this.client.close();
            result = new ActionProgress<Void>(null, ActionResult.SUCCESS);
        } catch (DBRequestException e) {
            e.printStackTrace();
            result = new ActionProgress<Void>(null, ActionResult.FAIL);
        }

        return result;
    }
}
