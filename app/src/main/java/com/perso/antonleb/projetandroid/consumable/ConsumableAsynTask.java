package com.perso.antonleb.projetandroid.consumable;

import android.os.AsyncTask;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public abstract class ConsumableAsynTask<Params, Progress, Result, Consumer extends IConsumer>
        extends AsyncTask<Params, Progress, Result>
        implements IConsumable<Consumer>
{
    protected final ConsumerManager<Consumer> consumers;

    public ConsumableAsynTask()
    {
        super();
        this.consumers = new ConsumerManager<Consumer>(this);
    }

    @Override
    public int consumerCount()
    {
        return consumers.consumerCount();
    }

    @Override
    public void clearConsumers()
    {
        consumers.clearConsumers();
    }

    @Override
    public boolean hasConsumer(IConsumer consumer)
    {
        return consumers.hasConsumer(consumer);
    }

    @Override
    public Consumer getConsumer(int index)
    {
        return consumers.getConsumer(index);
    }

    @Override
    public void removeConsumer(int index)
    {
        consumers.removeConsumer(index);
    }

    @Override
    public void removeConsumer(IConsumer consumer)
    {
        consumers.removeConsumer(consumer);
    }

    @Override
    public void addConsumer(Consumer consumer)
    {
        consumers.addConsumer(consumer);
    }
}
