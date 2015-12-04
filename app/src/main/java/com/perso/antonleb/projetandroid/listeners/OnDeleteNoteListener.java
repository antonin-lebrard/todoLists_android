package com.perso.antonleb.projetandroid.listeners;

import com.perso.antonleb.projetandroid.datas.ICategory;

/**
 * Created by antonleb on 02/12/2015.
 */
public interface OnDeleteNoteListener {
    /**
     * Action to do on deletion of the note
     * @param category the category in which the note was
     * @param note the deleted note
     */
    void onDeleteNote(ICategory category, String note);
}
