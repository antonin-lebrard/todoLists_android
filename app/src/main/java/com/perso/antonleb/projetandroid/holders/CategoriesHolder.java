package com.perso.antonleb.projetandroid.holders;

/**
 * Created by antonleb on 03/12/2015.
 */
public interface CategoriesHolder {
    /**
     * Add a category fragment.
     * @param categoryName the name of the category to add
     */
    void addCategory(String categoryName);
    /**
     * Add a note to a category.
     * Should be used only for activity launch
     * @param categoryName the name of the category to add the note into
     * @param noteName the name of the note to add
     */
    void addNote(String categoryName, String noteName);
}
