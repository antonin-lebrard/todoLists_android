package com.perso.antonleb.projetandroid.consumable;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Un binder qui supporte un mécanisme de consomation des résultats.
 */
public interface IConsumable<T extends IConsumer>
{
    /**
     * Ajoute un consommateur de résultat.
     *
     * @param consumer Consommateur de résultat.
     */
    public void addConsumer(T consumer);

    /**
     * Supprime un consommateur de résultat.
     *
     * @param consumer Consommateur de résultat.
     */
    public void removeConsumer(IConsumer consumer);

    /**
     * Supprime un consommateur de résultat.
     *
     * @throws IndexOutOfBoundsException
     *
     * @param index Index du consommateur à supprimer.
     */
    public void removeConsumer(int index);

    /**
     * Retourne un consommateur enregistré.
     *
     * @throws  IndexOutOfBoundsException
     *
     * @param index Index du consommateur à récupérer.
     *
     * @return Consommateur récupéré.
     */
    public T getConsumer(int index);

    /**
     * Vérifie qu'un consommateur de résultat est enregistré.
     *
     * @param consumer Consommateur de résultat recherché.
     *
     * @return boolean
     */
    public boolean hasConsumer(IConsumer consumer);

    /**
     * Supprime tous les consommateurs attachés.
     */
    public void clearConsumers();

    /**
     * Retourne le nombre de consommateurs enregistrés.
     *
     * @return Nombre de consommateurs enregistrés.
     */
    public int consumerCount();
}
