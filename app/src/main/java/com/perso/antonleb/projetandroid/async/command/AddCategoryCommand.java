package com.perso.antonleb.projetandroid.async.command;

import android.os.Parcel;

import com.perso.antonleb.projetandroid.datas.CategoryKey;
import com.perso.antonleb.projetandroid.datas.creators.SimpleCreator;
import com.perso.antonleb.projetandroid.utils.ParcelableUtils;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Une commande pour ajouter une catégorie.
 */
public final class AddCategoryCommand implements ICommand
{
    /**
     *  http://developer.android.com/reference/android/os/Parcelable.html
     */
    public final static SimpleCreator<AddCategoryCommand> CREATOR = SimpleCreator.getCreator(AddCategoryCommand.class);

    /**
     * Clef de la catégorie à ajouter.
     */
    private final CategoryKey category;

    /**
     * Créer une nouvelle catégorie.
     *
     * @param category
     */
    public AddCategoryCommand(CategoryKey category)
    {
        this.category = category;
    }

    /**
     * Créer une catégorie depuis un objet Parcel (sérialization)
     *
     * @param parcel
     */
    public AddCategoryCommand(Parcel parcel)
    {
        ParcelableUtils.assertIsValidParcel(parcel, this);

        this.category = new CategoryKey(parcel);
    }

    /**
     * @return Clef désignant la catégorie à supprimer.
     */
    public CategoryKey getCategory()
    {
        return category;
    }

    /**
     * Changer la catégorie à supprimer.
     *
     * @param newKey
     *
     * @return Nouvelle commande configurée.
     */
    public AddCategoryCommand setCategory(CategoryKey newKey)
    {
        if(newKey == null) {
            throw new IllegalArgumentException("Key can't be null.");
        }
        else {
            return new AddCategoryCommand(newKey);
        }
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        ParcelableUtils.writeParcelClassFlag(dest, this);
        this.category.writeToParcel(dest, flags);
    }
}
