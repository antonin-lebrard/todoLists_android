package com.perso.antonleb.projetandroid.data;

import android.os.Parcelable;

import com.perso.antonleb.projetandroid.exceptions.CategoryAlreadyExistException;
import com.perso.antonleb.projetandroid.exceptions.UserAlreadyExistException;

import java.util.Iterator;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Un utilisateur de l'application.
 */
public interface IUser extends Parcelable
{
    /**
     * Retourne le nom de l'utilisateur.
     * Le nom de l'utilisateur est UNIQUE.
     *
     * @return String Nom de l'utilisateur.
     */
    public String getName();

    /**
     * Retourne un itérateur sur les catégories de l'utilisateur.
     *
     * @return Itérateur sur les catégories de l'utilisateur.
     */
    public Iterator<ICategory> categoriesIterator();

    /**
     * Retourne une catégorie par son nom.
     *
     * @param name Nom de la catégorie.
     * @return Catégorie pour le nom donné.
     */
    public ICategory getCategory(String name);

    /**
     * Créé une nouvelle catégorie pour l'utilisateur.
     * Le nom choisi ne doit pas déjà exister.
     *
     * @param name Nom de la nouvelle catégorie.
     * @throws CategoryAlreadyExistException Levée si la catégorie existe déjà.
     *
     * @return ICategory Catégorie créée.
     */
    public ICategory createCategory(String name) throws CategoryAlreadyExistException;

    /**
     * Supprime une catégorie de l'utilisateur.
     *
     * @param name Nom de la catégorie à supprimer.
     */
    public void destroyCategory(String name);

    /**
     * Renome une catégorie de l'utilisateur.
     *
     * @throws CategoryAlreadyExistException Levée si le nouveau nom est déjà pris.
     *
     * @param oldName Ancien nom de la catégorie.
     * @param newName Nouveau nom de la catégorie.
     */
    public void renameCategory(String oldName, String newName) throws CategoryAlreadyExistException;

    /**
     * Retourne une clef vers cet utilisateur.
     *
     * @return Clef vers cet utilisateur.
     */
    public UserKey getKey();
}