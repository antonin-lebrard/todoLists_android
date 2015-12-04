package com.perso.antonleb.projetandroid.holders;

import com.perso.antonleb.projetandroid.listeners.GlobalNoteListener;

/**
 * Created by antonleb on 03/12/2015.
 */
public interface CategoriesViewHolder extends CategoriesHolder, GlobalNoteListener {
    /**
     * Push a note to the current Category (the one visible)
     * @param noteName the name of the note
     */
    void pushNoteToCurrent(String noteName);
    /**
     * Get the name of the current Category (the one visible)
     * @return the name of the current Category
     */
    String getCurrentCategoryName();
}
