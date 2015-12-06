package com.perso.antonleb.projetandroid.async;

import android.os.AsyncTask;
import android.os.Parcel;
import android.util.Log;

import com.perso.antonleb.projetandroid.async.command.ICommandQueue;
import com.perso.antonleb.projetandroid.async.command.ListCommandQueue;
import com.perso.antonleb.projetandroid.datas.creators.SimplePolymorphCreator;
import com.perso.antonleb.projetandroid.services.NoteDBService;
import com.perso.antonleb.projetandroid.utils.ParcelableUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public final class CloseDBServiceTask
        extends AsyncTask<Void, Void, Void>
{
    protected final NoteDBService service;

    public CloseDBServiceTask(NoteDBService service)
    {
        super();
        this.service = service;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ICommandQueue toSave = service.getCommandQueue();

        File file = new File(this.service.getFilesDir(), "COMMANDS_DB");
        Parcel parcel = Parcel.obtain();
        toSave.writeToParcel(parcel, 0);

        try {
            ParcelableUtils.toFile(this.service, parcel, file);
            parcel.recycle();
        } catch (IOException e) {
            parcel.recycle();
            e.printStackTrace();
        }

        //Log.i(ParcelableUtils.class.getCanonicalName(), "PLOPL ?");

        //Log.i(getClass().getCanonicalName(), "EXECUTED...");

        return null;
    }
}
