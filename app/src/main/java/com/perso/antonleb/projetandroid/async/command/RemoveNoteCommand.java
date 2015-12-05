package com.perso.antonleb.projetandroid.async.command;

import android.os.Parcel;

import com.perso.antonleb.projetandroid.datas.CategoryKey;
import com.perso.antonleb.projetandroid.datas.creators.SimpleCreator;
import com.perso.antonleb.projetandroid.utils.ParcelableUtils;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Une commande pour supprimer une note d'une catégorie.
 */
public final class RemoveNoteCommand implements ICommand
{
    /**
     *  http://developer.android.com/reference/android/os/Parcelable.html
     */
    public final static SimpleCreator<RemoveNoteCommand> CREATOR = SimpleCreator.getCreator(RemoveNoteCommand.class);

    /**
     * Clef de la catégorie à modifier.
     */
    private final CategoryKey category;

    /**
     * Note à supprimer.
     */
    private final String note;

    /**
     * Créer une nouvelle commande.
     *
     * @param category
     * @param note
     */
    public RemoveNoteCommand(CategoryKey category, String note)
    {
        this.category = category;
        this.note = note;
    }

    /**
     * Créer une commande depuis un objet Parcel (sérialization)
     *
     * @param parcel
     */
    public RemoveNoteCommand(Parcel parcel)
    {
        ParcelableUtils.assertIsValidParcel(parcel, this);

        this.category = new CategoryKey(parcel);
        this.note = parcel.readString();
    }

    /**
     * @return Clef désignant la catégorie à modifier.
     */
    public CategoryKey getCategory()
    {
        return category;
    }

    /**
     * Changer la catégorie à modifier.
     *
     * @param newKey
     *
     * @return Nouvelle commande configurée.
     */
    public RemoveNoteCommand setCategory(CategoryKey newKey)
    {
        if(newKey == null) {
            throw new IllegalArgumentException("Key can't be null.");
        }
        else {
            return new RemoveNoteCommand(newKey, this.note);
        }
    }

    /**
     * @return Note à supprimer.
     */
    public String getNote() {
        return note;
    }

    /**
     * Modifier la note à supprimer.
     *
     * @param note
     *
     * @return Nouvelle instance configurée.
     */
    public RemoveNoteCommand setNote(String note)
    {
        if(note == null || note.trim().equals("")) {
            throw new IllegalArgumentException("Note to remove can't be null or empty.");
        }
        else {
            return new RemoveNoteCommand(this.category, note);
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
        dest.writeString(this.note);
    }
}
