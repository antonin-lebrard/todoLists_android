package com.perso.antonleb.projetandroid.datas;

import android.os.Parcel;

import com.perso.antonleb.projetandroid.datas.creators.SimpleCreator;
import com.perso.antonleb.projetandroid.utils.ParcelableUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Un utilisateur de l'application.
 */
public class User implements IUser
{
    /**
     *  http://developer.android.com/reference/android/os/Parcelable.html
     */
    public final static SimpleCreator<User> CREATOR = SimpleCreator.getCreator(User.class);

    /**
     * Nom de l'utilisateur.
     */
    protected final String identifier;

    /**
     * Usage interne, évite d'instancier des clefs non stop.
     */
    protected CategoryKey key;

    /**
     * Catégories que l'utilisateur possède.
     */
    protected final Map<ICategory, ICategory> categories;

    /**
     * Créer un nouvel utilisateur avec un identifiant particulier.
     *
     * @param identifier Nom de l'utilisateur.
     */
    public User(String identifier)
    {
        this.identifier = identifier;
        this.categories = new HashMap<>();
        this.key = new CategoryKey();
    }

    /**
     * Créer un nouvel utilisateur depuis un objet Parcel.
     *
     * @param parcel
     */
    public User(Parcel parcel)
    {
        ParcelableUtils.assertIsValidParcel(parcel, this);

        this.key = new CategoryKey();
        this.categories = new HashMap<>();
        this.identifier = parcel.readString();

        ICategory[] readed = ParcelableUtils.readArray(parcel, ICategory.class);
        for(ICategory category : readed) this.categories.put(category, category);
    }

    /**
     * Retourne le nom de l'utilisateur.
     * Le nom de l'utilisateur est UNIQUE.
     *
     * @return String Nom de l'utilisateur.
     */
    @Override
    public String getName()
    {
        return this.identifier;
    }

    /**
     * Retourne une catégorie par son nom.
     *
     * @param name Nom de la catégorie.
     * @return Catégorie pour le nom donné.
     */
    @Override
    public ICategory getCategory(String name)
    {
        this.key.set(name, this.getKey());
        return this.categories.get(this.key);
    }

    /**
     * Retourne un itérateur sur les catégories de l'utilisateur.
     *
     * @return Iterator<ICategory> Itérateur sur les catégories de l'utilisateur.
     */
    @Override
    public Iterator<ICategory> categoriesIterator()
    {
        return this.categories.values().iterator();
    }

    /**
     * Ajouter une catégorie.
     *
     * @param category Nouvelle catégorie.
     *
     * @return ICategory Catégorie créée.
     */
    @Override
    public void addCategory(ICategory category) {
        this.categories.put(category, category);
    }

    /**
     * Supprime une catégorie de l'utilisateur.
     *
     * @param name Nom de la catégorie à supprimer.
     */
    @Override
    public void destroyCategory(String name)
    {
        this.key.set(name, this.getKey());
        this.categories.remove(this.key);
    }

    @Override
    public UserKey getKey() {
        return new UserKey(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (identifier != null ? !identifier.equals(user.identifier) : user.identifier != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User " + this.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelableUtils.writeParcelClassFlag(dest, this);
        dest.writeString(this.identifier);
        ParcelableUtils.writeArray(
                dest,
                flags,
                this.categories.values().toArray(new ICategory[this.categories.size()])
        );
    }
}