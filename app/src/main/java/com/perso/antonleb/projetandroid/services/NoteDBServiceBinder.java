package com.perso.antonleb.projetandroid.services;

import android.os.Binder;

import com.perso.antonleb.projetandroid.datas.CategoryKey;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.listeners.CommandResultListener;
import com.perso.antonleb.projetandroid.listeners.UserLoadingListener;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public final class NoteDBServiceBinder extends Binder
{
    protected final NoteDBService service;

    public NoteDBServiceBinder(NoteDBService service)
    {
        super();
        this.service = service;
    }

    public void loadUser(UserKey key, UserLoadingListener listener)
    {
        this.service.loadUser(key, listener);
    }

    public void addNote(CategoryKey key, String note)
    {
        this.service.addNote(key, note);
    }

    public void removeNote(CategoryKey key, String note)
    {
        this.service.removeNote(key, note);
    }

    public void addCategory(CategoryKey key)
    {
        this.service.addCategory(key);
    }
}
