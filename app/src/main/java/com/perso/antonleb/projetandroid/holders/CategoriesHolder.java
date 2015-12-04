package com.perso.antonleb.projetandroid.holders;

import java.util.Collection;

/**
 * Created by antonleb on 03/12/2015.
 */
public interface CategoriesHolder {
    /**
     * Add an empty category fragment.
     * @param categoryName the name of the category to add
     */
    void addCategory(String categoryName);
    /**
     * Add a category fragment with notes inside.
     * @param categoryName the name of the category to add
     * @param notes the notes to add directly to the category
     */
    void addCategory(String categoryName, Collection<String> notes);
    /**
     * Add a category fragment with notes inside.
     * @param categoryName the name of the category to add
     * @param notes the notes to add directly to the category
     */
    void addCategory(String categoryName, Iterable<String> notes);
    /**
     * Add a note to a category.
     * Should be used only for activity launch
     * @param categoryName the name of the category to add the note into
     * @param noteName the name of the note to add
     */
    void addNote(String categoryName, String noteName);
}
