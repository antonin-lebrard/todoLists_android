package com.perso.antonleb.projetandroid.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.perso.antonleb.projetandroid.datas.creators.SimplePolymorphCreator;

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
     * Ecrit une suite d'objets parcelables dans un objet Parcel.
     *
     * @param parcel
     * @param flags
     * @param objects
     */
    public static void writeArray(Parcel parcel, int flags, Parcelable[] objects)
    {
        parcel.writeInt(objects.length);
        for(Parcelable object : objects) object.writeToParcel(parcel, flags);
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
