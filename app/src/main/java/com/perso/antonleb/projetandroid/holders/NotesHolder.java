package com.perso.antonleb.projetandroid.holders;

/**
 * Created by antonleb on 04/12/2015.
 */
public interface NotesHolder {
    /**
     * Add a new note to the holder of these notes
     * @param noteName The note name
     * @return true if the operation was successful
     */
    boolean pushNote(String noteName);

    /**
     * Delete a note of the holder of these notes
     * @param noteName the name of the note to delete
     * @return true if the operation was successful
     */
    boolean deleteNote(String noteName);

    /**
     * Delete a note of the holder of these notes
     * @param position the position of the note to delete
     * @return true if the operation was successful
     */
    boolean deleteNote(int position);
}
