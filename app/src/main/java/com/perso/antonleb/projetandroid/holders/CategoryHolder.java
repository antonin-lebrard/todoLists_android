package com.perso.antonleb.projetandroid.holders;

import com.perso.antonleb.projetandroid.listeners.NotesListener;

/**
 * Created by antonleb on 04/12/2015.
 */
public interface CategoryHolder extends NotesHolder, NotesListener {
    /**
     * The name of the category
     * @return the name of the category
     */
    String getName();
}
