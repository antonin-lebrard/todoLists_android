package com.perso.antonleb.projetandroid.services;

import com.perso.antonleb.projetandroid.async.LoadUserTask;
import com.perso.antonleb.projetandroid.consumable.ConsumableBinder;
import com.perso.antonleb.projetandroid.datas.UserKey;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public final class NoteServiceBinder extends ConsumableBinder<INoteConsumer> implements INoteServiceBinder
{
    protected final NoteService service;

    public NoteServiceBinder(NoteService service)
    {
        super();
        this.service = service;
    }

    public void loadUser(UserKey key)
    {
        LoadUserTask task = this.service.loadUserTask();
        for(INoteConsumer consumer : this.consumerManager) {
            task.addConsumer(consumer);
        }
        task.execute(key);
    }
}
