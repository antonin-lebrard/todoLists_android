package com.perso.antonleb.projetandroid.async.command;

import android.os.Parcel;

import com.perso.antonleb.projetandroid.datas.creators.SimpleCreator;
import com.perso.antonleb.projetandroid.utils.ParcelableUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class ListCommandQueue implements ICommandQueue
{
    /**
     *  http://developer.android.com/reference/android/os/Parcelable.html
     */
    public final static SimpleCreator<ListCommandQueue> CREATOR = SimpleCreator.getCreator(ListCommandQueue.class);

    protected List<ICommand> commands;

    public ListCommandQueue()
    {
        this.commands = new LinkedList<>();
    }

    public ListCommandQueue(Parcel parcel)
    {
        ParcelableUtils.assertIsValidParcel(parcel, this);
        ICommand[] commands = ParcelableUtils.readArray(parcel, ICommand.class);

        this.commands = new LinkedList<>(Arrays.asList(commands));
    }

    @Override
    public void push(ICommand command) {
        this.commands.add(this.size(), command);
    }

    @Override
    public ICommand next() {
        return this.commands.get(0);
    }

    @Override
    public boolean hasNext() {
        return this.size() > 0;
    }

    @Override
    public int size() {
        return this.commands.size();
    }

    @Override
    public void clear() {
        this.commands.clear();
    }

    @Override
    public ICommand release() {
        return this.commands.remove(0);
    }

    @Override
    public Iterator<ICommand> iterator() {
        return this.commands.iterator();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelableUtils.writeParcelClassFlag(dest, this);
        ParcelableUtils.writeArray(dest, flags, this.commands.toArray(new ICommand[this.size()]));
    }
}
