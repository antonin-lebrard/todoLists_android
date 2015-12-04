package com.perso.antonleb.projetandroid.listeners;

/**
 * Created by antonleb on 04/12/2015.
 */
public interface NotesListener {
    /**
     * Set a listener on deletion of a note
     * @param listener the listener that performs the operation on deletion
     */
    void setOnDeleteNoteListener(OnDeleteNoteListener listener);

    /**
     * Set a listener on addition of a note
     * @param listener the listener that performs the operation on addition
     */
    void setOnAddNoteListener(OnAddNoteListener listener);
}
