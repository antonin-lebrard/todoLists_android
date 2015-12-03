package com.perso.antonleb.projetandroid;

import java.util.List;

/**
 * Created by antonleb on 09/11/2015.
 */
public interface ICategory {

    List<INote> getItems();
    String getName();
    INote addNote(String note);
    INote deleteNote(String note);
    INote deleteNote(int index);

}
