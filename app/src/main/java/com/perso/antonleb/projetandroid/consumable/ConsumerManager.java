package com.perso.antonleb.projetandroid.consumable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Une implémentation simple de IConsumable
 */
public class ConsumerManager<T extends IConsumer> implements IConsumable<T>, Iterable<T>
{
    protected final Set<T> consumers;
    protected final IConsumable<T> deferred;

    public ConsumerManager(IConsumable<T> deferred)
    {
        this.consumers = new HashSet<T>();
        this.deferred = deferred;
    }

    @Override
    public void addConsumer(T consumer)
    {
        this.consumers.add(consumer);
        consumer.onBind(this.deferred);
    }

    @Override
    public void removeConsumer(IConsumer consumer)
    {
        this.consumers.remove(consumer);
        consumer.onUnbind(this.deferred);
    }

    @Override
    public void removeConsumer(int index)
    {
        Iterator<T> consumers = this.consumers.iterator();

        for(int i = 0; i < index; ++i) consumers.next();

        IConsumer removed = consumers.next();
        removed.onUnbind(this.deferred);
        consumers.remove();
    }

    @Override
    public T getConsumer(int index)
    {
        Iterator<T> consumers = this.consumers.iterator();

        for(int i = 0; i < index; ++i) consumers.next();

        return consumers.next();
    }

    @Override
    public boolean hasConsumer(IConsumer consumer)
    {
        return this.consumers.contains(consumer);
    }

    @Override
    public void clearConsumers()
    {
        for(IConsumer consumer : this.consumers) consumer.onUnbind(this.deferred);
        this.consumers.clear();
    }

    @Override
    public int consumerCount() {
        return this.consumers.size();
    }

    @Override
    public Iterator<T> iterator() {
        return this.consumers.iterator();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.clearConsumers();
    }
}
