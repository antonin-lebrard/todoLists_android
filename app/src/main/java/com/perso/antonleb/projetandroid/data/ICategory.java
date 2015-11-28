package com.perso.antonleb.projetandroid.data;

import android.os.Parcelable;

import com.perso.antonleb.projetandroid.exceptions.CategoryAlreadyExistException;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Catégorie contenant plusieurs notes.
 */
public interface ICategory extends Iterable<String>, Parcelable
{
    /**
     * Retourne le nom de la catégorie.
     * Le nom de la catégorie est unique par utilisateur.
     *
     * @return Le nom de la catégorie.
     */
    public String getName();

    /**
     * Change le nom de la catégorie.
     *
     * @param name Nouveau nom de la catégorie.
     *
     * @throws CategoryAlreadyExistException Si le nouveau nom est déjà pris.
     */
    //public void setName(String name) throws CategoryAlreadyExistException;

    /**
     * Retourne l'utilisateur possédant cette catégorie.
     *
     * @return Possesseur de la catégorie.
     */
    public UserKey getOwner();

    /**
     * Ajoute une note à la catégorie.
     *
     * @param note Note à ajouter.
     */
    public void addNote(String note);

    /**
     * Supprime une note de la catégorie.
     *
     * @param note Note à supprimer.
     */
    public void removeNote(String note);

    /**
     * Supprime une note à un index particulier.
     *
     * @param index Index de la note à supprimer.
     */
    public void removeNote(int index);

    /**
     * Retourne une note à un index particulier.
     *
     * @param index Index de la note à retourner.
     * @return Note retrouvée.
     */
    public String getNote(int index);

    /**
     * Vérifie que la catégorie contient une note.
     *
     * @param note Note à rechercher.
     * @return boolean
     */
    public boolean hasNote(String note);

    /**
     * Retourne le nombre de notes dans la catégorie.
     *
     * @return Nombre de notes dans la catégorie.
     */
    public int size();

    /**
     * Retourne une clef permettant de récupérer cette catégorie.
     *
     * @return Clef vers cette catégorie.
     */
    public CategoryKey getCategoryKey();
}