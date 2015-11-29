package com.perso.antonleb.projetandroid;

import android.support.v7.app.AppCompatActivity;

import com.perso.antonleb.projetandroid.consumable.ConsumableManager;
import com.perso.antonleb.projetandroid.consumable.IConsumable;
import com.perso.antonleb.projetandroid.consumable.IConsumer;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class AppCompatConsumerActivity extends AppCompatActivity implements IConsumer
{
    public ConsumableManager consumableManager;

    public AppCompatConsumerActivity()
    {
        consumableManager = new ConsumableManager(this);
    }

    @Override
    public void onBind(IConsumable<?> consumable)
    {
        consumableManager.onBind(consumable);
    }

    @Override
    public void onUnbind(IConsumable<?> consumable)
    {
        consumableManager.onUnbind(consumable);
    }

    @Override
    public void unbindAll()
    {
        consumableManager.unbindAll();
    }

    @Override
    public void unbind(IConsumable<?> binded)
    {
        consumableManager.unbind(binded);
    }

    @Override
    public void unbind(int index)
    {
        consumableManager.unbind(index);
    }

    @Override
    public int binderCount()
    {
        return consumableManager.binderCount();
    }

    @Override
    public IConsumable<?> getBinder(int index)
    {
        return consumableManager.getBinder(index);
    }
}
