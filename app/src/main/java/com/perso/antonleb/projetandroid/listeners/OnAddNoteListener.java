package com.perso.antonleb.projetandroid.listeners;

import com.perso.antonleb.projetandroid.datas.ICategory;

/**
 * Created by antonleb on 02/12/2015.
 */
public interface OnAddNoteListener {
    /**
     * Action to do on addition of the note
     * @param category the category in which the note was added
     * @param note the added note
     */
    void onAddNote(ICategory category, String note);
}
