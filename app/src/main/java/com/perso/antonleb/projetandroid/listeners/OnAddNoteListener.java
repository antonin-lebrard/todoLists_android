package com.perso.antonleb.projetandroid.listeners;

import com.perso.antonleb.projetandroid.ICategorie;
import com.perso.antonleb.projetandroid.INote;

/**
 * Created by antonleb on 02/12/2015.
 */
public interface OnAddNoteListener {
    void onAddNote(ICategorie categorie, INote note);
}
