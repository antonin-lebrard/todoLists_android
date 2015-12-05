package com.perso.antonleb.projetandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.perso.antonleb.projetandroid.listeners.NetworkStateListener;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Permet de surveiller l'état de la connexion.
 */
public class NetworkStateReceiver extends BroadcastReceiver
{
    protected boolean offline;
    protected boolean singleton;
    protected NetworkStateListener listener;

    public static NetworkStateReceiver createFor(Context context)
    {
        NetworkStateReceiver result = new NetworkStateReceiver();
        context.registerReceiver(
                result,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        );
        return result;
    }

    public static NetworkStateReceiver createFor(Context context, NetworkStateListener listener)
    {
        NetworkStateReceiver result = new NetworkStateReceiver(listener);
        context.registerReceiver(
                result,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        );
        return result;
    }

    protected NetworkStateReceiver(NetworkStateListener listener)
    {
        super();
        this.offline = true;
        this.listener = listener;
        this.singleton = true;
    }

    protected NetworkStateReceiver()
    {
        super();
        this.offline = true;
        this.singleton = true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean oldOffline = this.offline;
        this.offline = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        if(this.listener != null && (oldOffline != this.offline || this.singleton)) {
            this.singleton = false;
            if(this.offline) {
                this.listener.onEnteringOfflineMode();
            }
            else {
                this.listener.onEnteringOnlineMode();
            }
        }
    }

    public boolean isOffline()
    {
        return offline;
    }

    public boolean isOnline()
    {
        return !offline;
    }
}
