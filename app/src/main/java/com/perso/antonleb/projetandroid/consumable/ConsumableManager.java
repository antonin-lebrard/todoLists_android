package com.perso.antonleb.projetandroid.consumable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class ConsumableManager implements IConsumer
{
    protected final Set<IConsumable<?>> consumables;
    protected final IConsumer deferred;

    public ConsumableManager(IConsumer deferred)
    {
        this.consumables = new HashSet<IConsumable<?>>();
        this.deferred = deferred;
    }

    @Override
    public void onBind(IConsumable<?> consumable)
    {
        this.consumables.add(consumable);
    }

    @Override
    public void onUnbind(IConsumable<?> consumable)
    {
        this.consumables.remove(consumable);
    }

    @Override
    public void unbindAll()
    {
        for(IConsumable consumable : this.consumables) consumable.removeConsumer(this.deferred);
    }

    @Override
    public void unbind(IConsumable<?> binded)
    {
        binded.removeConsumer(this.deferred);
    }

    @Override
    public void unbind(int index)
    {
        Iterator<IConsumable<?>> consumers = this.consumables.iterator();

        for(int i = 0; i < index; ++i) consumers.next();

        IConsumable<?> removed = consumers.next();
        removed.removeConsumer(this.deferred);
        consumers.remove();
    }

    @Override
    public int binderCount() {
        return this.consumables.size();
    }

    @Override
    public IConsumable<?> getBinder(int index) {
        Iterator<IConsumable<?>> consumers = this.consumables.iterator();

        for(int i = 0; i < index; ++i) consumers.next();

        return consumers.next();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.unbindAll();
    }
}
