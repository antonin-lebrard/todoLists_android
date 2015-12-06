package com.perso.antonleb.projetandroid.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.perso.antonleb.projetandroid.datas.creators.SimplePolymorphCreator;
import com.perso.antonleb.projetandroid.exceptions.DBRequestException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public final class ParcelableUtils
{
    /**
     * Vérifie qu'un objet Parcel est compatible.
     *
     * @param parcel
     * @param object
     */
    public static void assertIsValidParcel(Parcel parcel, Object object)
    {
        final String clazz = parcel.readString();

        if(!clazz.equals(object.getClass().getCanonicalName())) {
            throw new IllegalArgumentException(
                    "Parcel do not refer to a User object : "
                            + clazz
                            + " != "
                            + object.getClass().getCanonicalName()
            );
        }
    }

    /**
     * Flag permettant d'utiliser assertIsValidParcel.
     *
     * @param parcel
     * @param object
     */
    public static void writeParcelClassFlag(Parcel parcel, Object object)
    {
        parcel.writeString(object.getClass().getCanonicalName());
    }

    /**
     * Transforme un fichier en parcel.
     *
     * @param file
     *
     * @return
     */
    public static Parcel toParcel(Context context, File file) throws IOException {
        InputStream in = null;

        in = context.openFileInput(file.getName());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        int next = 0;
        while((next = in.read()) >= 0) {
            bytes.write(next);
        }

        in.close();
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes.toByteArray(), 0, bytes.size());
        parcel.setDataPosition(0);
        return parcel;
    }

    /**
     * Transforme un parcel en fichier.
     *
     * @param file
     */
    public static void toFile(Context context, Parcel parcel, File file) throws IOException {
        OutputStream output = context.openFileOutput(file.getName(), context.MODE_PRIVATE);

        Log.i(ParcelableUtils.class.getCanonicalName(), "SIZE TO WRITE " + parcel.dataSize());
        byte[] bytes = parcel.marshall();
        Log.i(ParcelableUtils.class.getCanonicalName(), "MARSHALLED");
        output.write(bytes);
        output.flush();
        output.close();
        Log.i(ParcelableUtils.class.getCanonicalName(), "WRITED");
    }

    /**
     * Ecrit une suite d'objets parcelables dans un objet Parcel.
     *
     * @param parcel
     * @param flags
     * @param objects
     */
    public static void writeArray(Parcel parcel, int flags, Parcelable[] objects)
    {
        parcel.writeInt(objects.length);
        for(Parcelable object : objects) {
            Log.i(ParcelableUtils.class.getCanonicalName(), "WRITING " + object);
            object.writeToParcel(parcel, flags);
        }
    }

    /**
     * Ecrit une suite d'objets parcelables dans un objet Parcel.
     *
     * @param parcel
     * @param clazz
     */
    public static <T> T[] readArray(Parcel parcel, Class<T> clazz)
    {
        SimplePolymorphCreator<T> creator = SimplePolymorphCreator.getCreator(clazz);
        final int size = parcel.readInt();
        LinkedList<T> objects = new LinkedList<>();

        for(int i = 0; i < size; ++i) {
            objects.add(creator.createFromParcel(parcel));
        }

        return objects.toArray(creator.newArray(objects.size()));
    }
}
