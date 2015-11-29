package com.perso.antonleb.projetandroid.consumable;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Un consommateur de résultat, objet qui est avare de callback.
 */
public interface IConsumer
{
    /**
     * Appellé quand le consommateur de donnée est attaché à un consommable.
     *
     * @param consumable
     */
    public void onBind(IConsumable<?> consumable);

    /**
     * Appellé quand le consommateur de donnée est détaché d'un consommable.
     *
     * @param consumable
     */
    public void onUnbind(IConsumable<?> consumable);

    /**
     * Détache tous les flux de consomables.
     */
    public void unbindAll();

    /**
     * Détache le consommateur d'un consomable.
     *
     * @param binded
     */
    public void unbind(IConsumable<?> binded);

    /**
     * Détache le consommateur d'un consomable.
     *
     * @throws IndexOutOfBoundsException
     *
     * @param index
     */
    public void unbind(int index);

    /**
     * Retourne le nombre de flux de consomables enregistrés.
     *
     * @return Le nombre de flux de consomables enregistrés.
     */
    public int binderCount();

    /**
     * Récupère un flux de consomable auxquel le binder est attaché.
     *
     * @throws IndexOutOfBoundsException
     *
     * @param index Index du binder à récupérer.
     *
     * @return Le flux de consomable à l'index demandé.
     */
    public IConsumable<?> getBinder(int index);
}
