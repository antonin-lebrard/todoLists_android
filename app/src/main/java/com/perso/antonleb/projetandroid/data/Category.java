package com.perso.antonleb.projetandroid.data;

import com.perso.antonleb.projetandroid.exceptions.CategoryAlreadyExistException;

import java.lang.IndexOutOfBoundsException;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Une catégorie.
 */
public class Category implements ICategory
{
    /**
     * Nom de la catégorie.
     */
    protected String name;

    /**
     * Notes
     */
    protected Set<String> notes;

    /**
     * Utilisateur possédant la catégorie.
     */
    protected IUser owner;

    /**
     * Créer une nouvelle catégorie avec un nom particulier.
     *
     * @param owner Possésseur de la catégorie.
     * @param name Nom de la nouvelle catégorie.
     */
    public Category(IUser owner, String name)
    {
        this.owner = owner;
        this.name = name;
        this.notes = new HashSet<>();
    }

    /**
     * Créer une copie d'une autre catégorie.
     *
     * @param toCopy Catégorie à copier.
     * @param newName Nom de la nouvelle catégorie.
     */
    public Category(ICategory toCopy, String newName)
    {
        this.owner = toCopy.getOwner();
        this.name = newName;
        this.notes = new HashSet<>();

        for(String note : toCopy) {
            this.notes.add(note);
        }
    }

    /**
     * Créer une catégorie avec des notes.
     *
     * @param owner Possésseur de la catégorie.
     * @param name Nom de la nouvelle catégorie.
     * @param notes Notes à ajouter.
     */
    public Category(IUser owner, String name, Collection<String> notes)
    {
        this.owner = owner;
        this.name = name;
        this.notes = new HashSet<>();

        for(String note : notes) {
            this.notes.add(note);
        }
    }

    /**
     * Créer une catégorie avec des notes.
     *
     * @param owner Possésseur de la catégorie.
     * @param name Nom de la nouvelle catégorie.
     * @param notes Notes à ajouter.
     */
    public Category(IUser owner, String name, Iterable<String> notes)
    {
        this.owner = owner;
        this.name = name;
        this.notes = new HashSet<>();

        for(String note : notes) {
            this.notes.add(note);
        }
    }

    /**
     * Vérifie que l'index est valide. Dans le cas contraire cette méthode émet une exception.
     *
     * @param index Index à valider.
     *
     * @throws IndexOutOfBoundsException
     */
    protected void assertIsValidIndex(int index) throws IndexOutOfBoundsException
    {
        if(index < 0 || index >= this.notes.size()) {
            throw new IndexOutOfBoundsException(
                    "Index out of range, " + index + " not in [" + 0 + "; "
                            + (this.notes.size()-1) + "]"
            );
        }
    }

    /**
     * Retourne le nom de la catégorie.
     * Le nom de la catégorie est unique par utilisateur.
     *
     * @return String Le nom de la catégorie.
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * Change le nom de la catégorie.
     *
     * @param name Nouveau nom de la catégorie.
     *
     * @throws CategoryAlreadyExistException Si le nouveau nom est déjà pris.
     */
    @Override
    public void setName(String name) throws CategoryAlreadyExistException
    {
        this.name = name;
    }

    /**
     * Retourne l'utilisateur possédant cette catégorie.
     *
     * @return IUser Possesseur de la catégorie.
     */
    @Override
    public IUser getOwner()
    {
        return this.owner;
    }

    /**
     * Ajoute une note à la catégorie.
     *
     * @param note Note à ajouter.
     */
    @Override
    public void addNote(String note)
    {
        this.notes.add(note);
    }

    /**
     * Supprime une note de la catégorie.
     *
     * @param note Note à supprimer.
     */
    @Override
    public void removeNote(String note)
    {
        this.notes.remove(note);
    }

    /**
     * Supprime une note à un index particulier.
     *
     * @param index Index de la note à supprimer.
     */
    @Override
    public void removeNote(int index)
    {
        this.assertIsValidIndex(index);

        Iterator<String> iterator = this.notes.iterator();

        for(int i = 0; i < index; ++i) iterator.next();

        iterator.remove();
    }

    /**
     * Retourne une note à un index particulier.
     *
     * @param index Index de la note à retourner.
     * @return String Note retrouvée.
     */
    @Override
    public String getNote(int index)
    {
        this.assertIsValidIndex(index);

        Iterator<String> iterator = this.notes.iterator();

        for(int i = 0; i < index; ++i) iterator.next();

        return iterator.next();
    }

    /**
     * Vérifie que la catégorie contient une note.
     *
     * @param note Note à rechercher.
     * @return boolean
     */
    @Override
    public boolean hasNote(String note)
    {
        return this.notes.contains(note);
    }

    /**
     * Retourne le nombre de notes dans la catégorie.
     *
     * @return Nombre de notes dans la catégorie.
     */
    @Override
    public int size()
    {
        return this.notes.size();
    }

    @Override
    public String toString() {
        return "Category " + this.getName() + "@" + this.owner.getName();
    }

    /**
     * Retourne une clef permettant de récupérer cette catégorie.
     *
     * @return CategoryKey Clef vers cette catégorie.
     */
    @Override
    public CategoryKey getCategoryKey()
    {
        return new CategoryKey(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null) return false;

        if(o instanceof CategoryKey) {
            return o.equals(this);
        }
        else if(o instanceof ICategory) {
            ICategory category = ICategory.class.cast(o);
            return name.equals(category.getName()) && owner.equals(category.getOwner());
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    @Override
    public Iterator<String> iterator() {
        return this.notes.iterator();
    }
}
