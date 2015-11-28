package com.perso.antonleb.projetandroid.data;

import com.perso.antonleb.projetandroid.exceptions.CategoryAlreadyExistException;
import com.perso.antonleb.projetandroid.exceptions.UserAlreadyExistException;

import java.lang.Override;

import java.lang.String;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Un utilisateur de l'application.
 */
public class User implements IUser
{
    /**
     * Nom de l'utilisateur.
     */
    protected String identifier;

    /**
     * Usage interne, évite d'instancier des clefs non stop.
     */
    protected CategoryKey key;

    /**
     * Catégories que l'utilisateur possède.
     */
    protected Map<ICategory, ICategory> categories;

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
     * Change le nom de l'utilisateur.
     * Le nom choisi ne doit pas déjà exister dans l'application.
     *
     * @throws UserAlreadyExistException Si le nouveau nom est déjà pris.
     */
    @Override
    public void setName(String newName) throws UserAlreadyExistException
    {
        this.identifier = newName;
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
        this.key.set(name, this);
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
     * Créé une nouvelle catégorie pour l'utilisateur.
     * Le nom choisi ne doit pas déjà exister.
     *
     * @param name Nom de la nouvelle catégorie.
     * @throws CategoryAlreadyExistException Levée si la catégorie existe déjà.
     *
     * @return ICategory Catégorie créée.
     */
    @Override
    public ICategory createCategory(String name) throws CategoryAlreadyExistException
    {
        Category category = new Category(this, name);
        this.categories.put(category, category);
        return category;
    }

    /**
     * Supprime une catégorie de l'utilisateur.
     *
     * @param name Nom de la catégorie à supprimer.
     */
    @Override
    public void destroyCategory(String name)
    {
        this.key.set(name, this);
        this.categories.remove(this.key);
    }

    /**
     * Renome une catégorie de l'utilisateur.
     *
     * @throws CategoryAlreadyExistException Levée si le nouveau nom est déjà pris.
     *
     * @param oldName Ancien nom de la catégorie.
     * @param newName Nouveau nom de la catégorie.
     */
    @Override
    public void renameCategory(String oldName, String newName) throws CategoryAlreadyExistException
    {
        // @TODO
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
}