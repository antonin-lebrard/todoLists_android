package com.perso.antonleb.projetandroid.listeners;

/**
 * Created by antonleb on 03/12/2015.
 */
public interface GlobalNoteListener {
    /**
     * Add a listener on note deletion
     * @param listener the delete listener to apply on each note
     */
    void setGlobalOnDeleteNoteListener(OnDeleteNoteListener listener);
    /**
     * Add a listener on note insertion
     * @param listener the add listener to apply on each note
     */
    void setGlobalOnAddNoteListener(OnAddNoteListener listener);
}
