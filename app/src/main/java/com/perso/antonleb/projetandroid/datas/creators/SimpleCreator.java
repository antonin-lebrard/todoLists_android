package com.perso.antonleb.projetandroid.datas.creators;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Simple standard parcelable object creator.
 * http://developer.android.com/reference/android/os/Parcelable.html
 */
public class SimpleCreator<T> implements Parcelable.Creator<T>
{
    protected Class<T> clazz;
    protected Constructor<T> constructor;

    public SimpleCreator(Class<T> clazz)
    {
        this.clazz = clazz;
        this.captureConstructor(clazz);
    }

    /**
     * Capture a constructor to instantiate object.
     *
     * @param clazz
     */
    protected void captureConstructor(Class<T> clazz)
    {
        try {
            this.constructor = clazz.getConstructor(Parcel.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "Class do not have a Parcel constructor : " + clazz,
                    e
            );
        }
    }

    @Override
    public T createFromParcel(Parcel source)
    {
        try {
            return this.constructor.newInstance(source);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T[] newArray(int size)
    {
        return (T[]) Array.newInstance(this.clazz, size);
    }

    public static <T> SimpleCreator<T> getCreator(Class<T> clazz)
    {
        return new SimpleCreator<T>(clazz);
    }
}
