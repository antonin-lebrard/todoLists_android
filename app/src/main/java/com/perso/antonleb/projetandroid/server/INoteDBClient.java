package com.perso.antonleb.projetandroid.server;

import com.perso.antonleb.projetandroid.datas.CategoryKey;
import com.perso.antonleb.projetandroid.datas.ICategory;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.exceptions.DBRequestException;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Serveur permettant d'échanger des données avec l'application.
 */
public interface INoteDBClient
{
    /**
     * Ouvre le client.
     */
    public void open() throws DBRequestException;

    /**
     * Retourne un utilisateur récupéré sur le serveur.
     * Créée l'utilisateur s'il n'existe pas.
     *
     * @param key Clef permettant d'identifier l'utilisateur.
     *
     * @return L'utilisateur chargé.
     */
    public IUser getUser(UserKey key) throws DBRequestException;

    /**
     * Change complètement l'état d'un utilisateur.
     *
     * @param user
     *
     * @throws DBRequestException
     */
    public void setUser(IUser user) throws DBRequestException;

    /**
     * Ajoute une note dans une catégorie.
     *
     * @param categoryKey Identifiant de la catégorie.
     * @param note Note à ajouter.
     */
    public void addNote(CategoryKey categoryKey, String note) throws DBRequestException;

    /**
     * Supprime une note d'une catégorie.
     *
     * @param categoryKey Identifiant de la catégorie.
     * @param note Note à supprimer.
     */
    public void removeNote(CategoryKey categoryKey, String note) throws DBRequestException;

    /**
     * Créer une nouvelle catégorie
     *
     * @param key Catégorie à créer.
     */
    public void createCategory(CategoryKey key) throws DBRequestException;

    /**
     * Ferme le client.
     */
    public void close() throws DBRequestException;
}