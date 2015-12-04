package com.perso.antonleb.projetandroid.holders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;

import com.perso.antonleb.projetandroid.ICategory;
import com.perso.antonleb.projetandroid.INote;
import com.perso.antonleb.projetandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonleb on 09/11/2015.
 */
public class CategoryFragmentHolderImpl extends CategoryFragmentHolder {

    private View placeholder;
    private ViewPropertyAnimator animator;

    public static String ARG_NOTES = "notes";

    public static CategoryFragmentHolderImpl newInstance(String categoryName){
        return newInstance(categoryName, new ArrayList<String>());
    }

    public static CategoryFragmentHolderImpl newInstance(String categoryName, ArrayList<String> notes){
        CategoryFragmentHolderImpl frag = new CategoryFragmentHolderImpl();
        frag.category = new CategorieImpl(categoryName);
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_NOTES, notes);
        frag.setArguments(args);
        return frag;
    }

    public CategoryFragmentHolderImpl(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        placeholder = rootView.findViewById(R.id.text_placeholder_empty_note);

        animator = placeholder.animate().alpha(1.0f).setDuration(500).setStartDelay(500).setInterpolator(new DecelerateInterpolator());

        ArrayList<String> notes = getArguments().getStringArrayList(ARG_NOTES);
        if (notes != null) {
            for (String note : notes)
                category.addNote(note);
        }

        if (category.getItems().size() == 0){
            placeholder.setVisibility(View.VISIBLE);
            animator.start();
        }

        View mView = getView();
        if (mView != null)
            if (this.getUserVisibleHint())
                getActivity().setTitle(capitalize(this.getName()));

        return rootView;
    }

    @Override
    public boolean pushNote(String noteName) {
        boolean added = super.pushNote(noteName);
        if (added && animator != null){
            animator.cancel();
            placeholder.setVisibility(View.INVISIBLE);
        }
        return added;
    }

    @Override
    public boolean deleteNote(String noteName) {
        boolean deleted = super.deleteNote(noteName);
        if (deleted && category.getItems().size() == 0){
            placeholder.setVisibility(View.VISIBLE);
            animator.start();
        }
        return deleted;
    }

    @Override
    public boolean deleteNote(int position) {
        boolean deleted = super.deleteNote(position);
        if (deleted && category.getItems().size() == 0){
            placeholder.setVisibility(View.VISIBLE);
            animator.start();
        }
        return deleted;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible)
            if (getActivity() != null)
                getActivity().setTitle(capitalize(this.getName()));
    }

    @Override
    public void onResume() {
        super.onResume();
        View mView = getView();
        if (mView != null)
            if (this.getUserVisibleHint())
                getActivity().setTitle(capitalize(this.getName()));
    }

    private String capitalize(String toCapitalize){
        String capitalized = toCapitalize;
        if (capitalized.length() == 1)
            capitalized = capitalized.toUpperCase();
        if (capitalized.length() > 1)
            capitalized = capitalized.substring(0,1).toUpperCase() + capitalized.substring(1);
        return capitalized;
    }

}

class CategorieImpl implements ICategory {
    String name;
    ArrayList<INote> notes = new ArrayList<INote>();

    public CategorieImpl(String name){ this.name = name; }

    public List<INote> getItems(){ return notes; }
    public String getName() { return this.name; }
    public INote addNote(String note){
        if (note.equals("")) return null;
        NoteImpl newNote = new NoteImpl(note);
        if (!notes.contains(newNote)){
            notes.add(newNote);
            return newNote;
        }
        return null;
    }
    public INote deleteNote(String note){
        if (note.equals("")) return null;
        for (int i = 0; i < notes.size(); i++){
            if (notes.get(i).hashCode() == note.hashCode()){
                INote returning = notes.get(i);
                notes.remove(i);
                return returning;
            }
        }
        return null;
    }
    public INote deleteNote(int index){
        if (index < notes.size() && index > -1){
            INote returning = notes.get(index);
            notes.remove(index);
            return returning;
        }
        return null;
    }
}

class NoteImpl implements INote {
    String note;
    public NoteImpl(String note){ this.note = note; }
    public NoteImpl(){ this.note = ""; }

    public String getNote() { return note; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteImpl)) return false;
        NoteImpl note1 = (NoteImpl) o;
        return !(note != null ? !note.equals(note1.note) : note1.note != null);
    }

    @Override
    public int hashCode() {
        return note != null ? note.hashCode() : 0;
    }
}