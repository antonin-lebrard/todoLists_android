package com.perso.antonleb.projetandroid.datas;

import android.os.Parcel;
import android.os.Parcelable;

import com.perso.antonleb.projetandroid.datas.creators.SimpleCreator;

import java.lang.Override;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Cette classe permet d'identifier des objets ICategory de manière unique.
 */
public final class CategoryKey implements Parcelable
{
    /**
     *  http://developer.android.com/reference/android/os/Parcelable.html
     */
    public static final Parcelable.Creator<CategoryKey> CREATOR
            = SimpleCreator.getCreator(CategoryKey.class);

    /**
     * Nom de la catégorie.
     */
    public String categoryName;

    /**
     * Utilisateur possédant la catégorie.
     */
    public UserKey owner;

    /**
     * Créer une clef vide.
     */
    public CategoryKey()
    {
        this.set(null, null);
    }

    /**
     * Créer une clef depuis une catégorie existente.
     *
     * @param category Catégorie sur laquelle va pointer la clef.
     */
    public CategoryKey(ICategory category)
    {
        this.set(category);
    }

    /**
     * Créer une clef depuis une autre clef.
     *
     * @param other Clef à copier.
     */
    public CategoryKey(CategoryKey other)
    {
        this.set(other);
    }

    /**
     * Créer une clef custom.
     *
     * @param categoryName Nom de la catégorie recherchée.
     * @param owner Possésseur de la catégorie recherchée.
     */
    public CategoryKey(String categoryName, UserKey owner)
    {
        this.set(categoryName, owner);
    }

    /**
     * Créer une clef depuis un objet parcel.
     *
     * @param parcel
     */
    public CategoryKey(Parcel parcel)
    {
        this.set(
                parcel.readString(),
                new UserKey(parcel)
        );
    }

    /**
     * Change la clef existente en une clef custom.
     *
     * @param categoryName Nom de la catégorie recherchée.
     * @param owner Possésseur de la catégorie recherchée.
     */
    public void set(String categoryName, UserKey owner)
    {
        this.categoryName = categoryName;
        this.owner = owner;
    }

    /**
     * Change la clef en une autre clef existente.
     *
     * @param other Clef à copier.
     */
    public void set(CategoryKey other)
    {
        if(other == null) this.set(null, null);
        else this.set(other.categoryName, other.owner);
    }

    /**
     * Change la clef en clef vers une catégorie spécifique.
     *
     * @param category Catégorie sur laquelle va pointer la clef.
     */
    public void set(ICategory category)
    {
        if(category == null) this.set(null, null);
        else {
            this.categoryName = category.getName();
            this.owner.set(category.getOwner());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if(o instanceof CategoryKey) {
            return this.equals(CategoryKey.class.cast(o));
        }
        else if (o instanceof ICategory) {
            return this.equals(ICategory.class.cast(o));
        }
        else {
            return false;
        }
    }

    /**
     * Vérifie que cette clef est équivalente à la clef passée en paramètre.
     * @param other Clef à comparer.
     * @return boolean
     */
    private boolean equals(CategoryKey other)
    {
        if(
            (categoryName != null && !categoryName.equals(other.categoryName))
            || (categoryName == null && other.categoryName != null)
        ) return false;

        if (
            (owner != null && !owner.equals(other.owner))
            || (owner == null & other.owner != null)
        ) return false;

        return true;
    }

    /**
     * Vérifie que la clef match la catégorie.
     * @param other Catégorie à comparer.
     * @return boolean
     */
    private boolean equals(ICategory other)
    {
        if(
            (categoryName != null && !categoryName.equals(other.getName()))
            || (categoryName == null && other.getName() != null)
        ) return false;

        if (
            (owner != null && !owner.equals(other.getOwner()))
            || (owner == null & other.getOwner() != null)
        ) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = categoryName != null ? categoryName.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CategoryKey "  + this.categoryName + "@" + this.owner.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryName);
        this.owner.writeToParcel(dest, flags);
    }
}