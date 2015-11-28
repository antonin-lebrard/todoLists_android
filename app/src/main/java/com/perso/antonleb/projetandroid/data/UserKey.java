package com.perso.antonleb.projetandroid.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.perso.antonleb.projetandroid.data.creators.SimpleCreator;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Clef permettant d'identifier un utilisateur.
 */
public final class UserKey implements Parcelable
{
    /**
     *  http://developer.android.com/reference/android/os/Parcelable.html
     */
    public static final Parcelable.Creator<UserKey> CREATOR
            = SimpleCreator.getCreator(UserKey.class);

    /**
     * Nom de l'utilisateur UNIQUE.
     */
    public String name;

    /**
     * Créer une clef vide.
     */
    public UserKey()
    {
        this.name = null;
    }

    /**
     * Créer une clef custom.
     *
     * @param name Nom de l'utilisateur recherché.
     */
    public UserKey(String name)
    {
        this.name = name;
    }

    /**
     * Créer une clef vers un utilisateur.
     *
     * @param user Utilisateur.
     */
    public UserKey(IUser user)
    {
        this.name = user.getName();
    }

    /**
     * Créer une clef copie d'une autre clef.
     *
     * @param other Clef à copier.
     */
    public UserKey(UserKey other)
    {
        this.name = other.name;
    }

    /**
     * Créer une clef depuis un objet Parcel.
     *
     * @param in
     */
    public UserKey(Parcel in)
    {
        this.name = in.readString();
    }

    /**
     * Change la clef en clef custom.
     *
     * @param name Nom de l'utilisateur recherché.
     */
    public void set(String name)
    {
        this.name = name;
    }

    /**
     * Change la clef en clef vers un utilisateur existant.
     *
     * @param user Utilisateur.
     */
    public void set(IUser user)
    {
        this.name = user.getName();
    }

    /**
     * Change la clef pour qu'elle soit égale à une clef existente.
     *
     * @param other Clef a copier.
     */
    public void set(UserKey other)
    {
        this.name = other.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if(o instanceof IUser) {
            return this.equals((IUser) o);
        }
        else if(o instanceof  UserKey) {
            return this.equals((UserKey) o);
        }
        else {
            return false;
        }
    }

    /**
     * Vérifie l'égalitée entre cette clef et une autre clef.
     *
     * @param other Autre clef.
     * @return boolean
     */
    private boolean equals(UserKey other)
    {
        return !(name != null ? !name.equals(other.name) : other.name != null);
    }

    /**
     * Vérifie l'égalitée entre cette clef et un autre utilisateur.
     *
     * @param other Autre utilisateur.
     * @return boolean
     */
    private boolean equals(IUser other)
    {
        return !(name != null ? !name.equals(other.getName()) : other.getName() != null);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserKey @" + this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }
}
