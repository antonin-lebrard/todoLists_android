package com.perso.antonleb.projetandroid.server;

import com.perso.antonleb.projetandroid.data.CategoryKey;
import com.perso.antonleb.projetandroid.data.ICategory;
import com.perso.antonleb.projetandroid.data.IUser;
import com.perso.antonleb.projetandroid.exceptions.ServerRequestException;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Serveur permettant d'échanger des données avec l'application.
 */
public interface INoteServerClient
{
    /**
     * Retourne un utilisateur récupéré sur le serveur.
     * Créée l'utilisateur s'il n'existe pas.
     *
     * @param identifier Nom de l'utilisateur, unique.
     *
     * @return L'utilisateur chargé.
     */
    public IUser getUser(String identifier) throws ServerRequestException;

    /**
     * Ajoute une note dans une catégorie.
     *
     * @param categoryKey Identifiant de la catégorie.
     * @param note Note à ajouter.
     */
    public void addNote(CategoryKey categoryKey, String note) throws ServerRequestException;

    /**
     * Supprime une note d'une catégorie.
     *
     * @param categoryKey Identifiant de la catégorie.
     * @param note Note à supprimer.
     */
    public void removeNote(CategoryKey categoryKey, String note) throws ServerRequestException;

    /**
     * Supprime une catégorie complète du serveur.
     *
     * @param category Catégorie à supprimer.
     */
    public void removeCategory(ICategory category) throws ServerRequestException;

    /**
     * Récupère ou créée une catégorie depuis le serveur.
     *
     * @param key Identifiant de la catégorie.
     * @return Catégorie récupérée / créée.
     */
    public ICategory getCategory(CategoryKey key) throws ServerRequestException;
}