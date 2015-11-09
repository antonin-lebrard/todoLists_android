package com.perso.antonleb.projetandroid;

import java.util.List;

/**
 * Created by antonleb on 09/11/2015.
 */
public interface ICategorie {

    List<INote> getItems();
    String getName();
    boolean addNote(String note);
    boolean deleteNote(String note);
    boolean deleteNote(int index);

}
