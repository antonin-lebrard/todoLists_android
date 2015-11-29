package com.perso.antonleb.projetandroid.datas.creators;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Simple standard polymorph parcelable object creator.
 * http://developer.android.com/reference/android/os/Parcelable.html
 */
public class SimplePolymorphCreator<T> implements Parcelable.Creator<T>
{
    protected final Class<T> clazz;

    public SimplePolymorphCreator(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    /**
     * Check a class.
     *
     * @param clazzName
     * @return Class<? extends T>
     */
    protected Class<? extends T> assertIsValidClass(String clazzName)
    {
        Class<?> clazz = null;

        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                    "The class name do not match any class : " + clazzName,
                    e
            );
        }

        return clazz.asSubclass(this.clazz);
    }

    /**
     * Capture a constructor to instantiate object.
     *
     * @param clazz
     */
    protected Constructor<? extends T> captureConstructor(Class<? extends T> clazz)
    {
        try {
            return clazz.getConstructor(Parcel.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "Class do not have a Parcel constructor : " + clazz,
                    e
            );
        }
    }

    @Override
    public T createFromParcel(Parcel source) {
        final int startPos = source.dataPosition();
        final String clazzName = source.readString();

        source.setDataPosition(startPos);

        Class<? extends T> clazz = this.assertIsValidClass(clazzName);
        Constructor<? extends T> constructor = this.captureConstructor(clazz);

        try {
            return constructor.newInstance(source);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T[] newArray(int size) {
        return (T[]) Array.newInstance(clazz, size);
    }

    public static <T> SimplePolymorphCreator<T> getCreator(Class<T> clazz)
    {
        return new SimplePolymorphCreator<T>(clazz);
    }
}
