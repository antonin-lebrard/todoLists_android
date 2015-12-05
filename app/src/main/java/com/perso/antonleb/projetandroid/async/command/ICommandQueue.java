package com.perso.antonleb.projetandroid.async.command;

import android.os.Parcelable;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public interface ICommandQueue extends Parcelable, Iterable<ICommand>
{
    /**
     * Ajouter une commande dans la file.
     *
     * @param command
     */
    public void push(ICommand command);

    /**
     * @return La prochaine commande à lancer, sans la supprimer.
     */
    public ICommand next();

    /**
     * @return true Si la taille de la file n'est pas nulle.
     */
    public boolean hasNext();

    /**
     * @return Nombre de commande restantes.
     */
    public int size();

    /**
     * Vide la file.
     */
    public void clear();

    /**
     * @return La prochaine commande à lancer, la supprime.
     */
    public ICommand release();
}
