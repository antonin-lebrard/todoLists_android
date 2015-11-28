package com.perso.antonleb.projetandroid.server;

import com.perso.antonleb.projetandroid.data.CategoryKey;
import com.perso.antonleb.projetandroid.data.ICategory;
import com.perso.antonleb.projetandroid.data.IUser;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Serveur permettant d'échanger des données avec l'application.
 */
public interface INoteServer
{
    /**
     * Retourne un utilisateur récupéré sur le serveur.
     * Créée l'utilisateur s'il n'existe pas.
     *
     * @param identifier Nom de l'utilisateur, unique.
     *
     * @return L'utilisateur chargé.
     */
    public IUser getUser(String identifier);

    /**
     * Ajoute une note dans une catégorie.
     *
     * @param categoryKey Identifiant de la catégorie.
     * @param note Note à ajouter.
     */
    public void addNote(CategoryKey categoryKey, String note);

    /**
     * Supprime une note d'une catégorie.
     *
     * @param categoryKey Identifiant de la catégorie.
     * @param note Note à supprimer.
     */
    public void removeNote(CategoryKey categoryKey, String note);

    /**
     * Supprime une catégorie complète du serveur.
     *
     * @param category Catégorie à supprimer.
     */
    public void removeCategory(ICategory category);

    /**
     * Récupère ou créée une catégorie depuis le serveur.
     *
     * @param key Identifiant de la catégorie.
     * @return Catégorie récupérée / créée.
     */
    public ICategory getCategory(CategoryKey key);
}