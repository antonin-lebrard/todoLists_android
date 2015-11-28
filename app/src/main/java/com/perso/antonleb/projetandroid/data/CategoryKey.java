package com.perso.antonleb.projetandroid.data;

import java.lang.Override;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Cette classe permet d'identifier des objets ICategory de manière unique. Utiliser pour
 * requêter les Set de catégories.
 */
public final class CategoryKey
{
    /**
     * Nom de la catégorie.
     */
    public String categoryName;

    /**
     * Utilisateur possédant la catégorie.
     */
    public IUser owner;

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
    public CategoryKey(String categoryName, IUser owner)
    {
        this.set(categoryName, owner);
    }

    /**
     * Change la clef existente en une clef custom.
     *
     * @param categoryName Nom de la catégorie recherchée.
     * @param owner Possésseur de la catégorie recherchée.
     */
    public void set(String categoryName, IUser owner)
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
        else this.set(category.getName(), category.getOwner());
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
}