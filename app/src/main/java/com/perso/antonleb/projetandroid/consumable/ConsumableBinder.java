package com.perso.antonleb.projetandroid.consumable;

import android.os.Binder;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class ConsumableBinder<T extends IConsumer> extends Binder implements IConsumable<T>
{
    protected ConsumerManager<T> consumerManager;

    public ConsumableBinder()
    {
        this.consumerManager = new ConsumerManager<T>(this);
    }

    @Override
    public int consumerCount()
    {
        return consumerManager.consumerCount();
    }

    @Override
    public void clearConsumers()
    {
        consumerManager.clearConsumers();
    }

    @Override
    public boolean hasConsumer(IConsumer consumer)
    {
        return consumerManager.hasConsumer(consumer);
    }

    @Override
    public T getConsumer(int index)
    {
        return consumerManager.getConsumer(index);
    }

    @Override
    public void removeConsumer(int index)
    {
        consumerManager.removeConsumer(index);
    }

    @Override
    public void removeConsumer(IConsumer consumer)
    {
        consumerManager.removeConsumer(consumer);
    }

    @Override
    public void addConsumer(T consumer)
    {
        consumerManager.addConsumer(consumer);
    }
}
