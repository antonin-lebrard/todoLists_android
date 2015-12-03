package com.perso.antonleb.projetandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;

import com.perso.antonleb.projetandroid.listeners.OnAddNoteListener;
import com.perso.antonleb.projetandroid.listeners.OnDeleteNoteListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonleb on 09/11/2015.
 */
public class CategoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnDeleteNoteListener onDeleteNoteListener;
    private OnAddNoteListener onAddNoteListener;

    private View placeholder;
    private boolean placeholderVisible = false;

    private ViewPropertyAnimator animator;

    ICategory categorie;

    public static String ARG_CATEGORIE_NAME = "categorieName";
    public static String ARG_NOTES = "notes";

    public static CategoryFragment newInstance(String categorieName){
        return newInstance(categorieName, new ArrayList<String>());
    }

    public static CategoryFragment newInstance(String categorieName, ArrayList<String> notes){
        CategoryFragment frag = new CategoryFragment();
        frag.categorie = new CategorieImpl(categorieName);
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_NOTES, notes);
        frag.setArguments(args);
        return frag;
    }

    public CategoryFragment(){}

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible)
            if (getActivity() != null)
                getActivity().setTitle(capitalize(this.categorie.getName()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categorie, container, false);
        placeholder = rootView.findViewById(R.id.text_placeholder_empty_note);

        placeholder.setVisibility(View.VISIBLE);
        placeholderVisible = true;
        animator = placeholder.animate().alpha(1.0f).setDuration(500).setStartDelay(500).setInterpolator(new DecelerateInterpolator());
        animator.start();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.listNotes);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> notes = getArguments().getStringArrayList(ARG_NOTES);
        if (notes != null) {
            if (!placeholderVisible) placeholder.setVisibility(View.INVISIBLE);
            for (String note : notes)
                categorie.addNote(note);
        }

        this.mAdapter = new NoteAdapter(this, this.categorie.getItems());
        mRecyclerView.setAdapter(mAdapter);

        View mView = getView();
        if (mView != null)
            if (this.getUserVisibleHint())
                getActivity().setTitle(capitalize(this.categorie.getName()));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        View mView = getView();
        if (mView != null)
            if (this.getUserVisibleHint())
                getActivity().setTitle(capitalize(this.categorie.getName()));
    }

    public boolean deleteNote(int position){
        INote deletedNote = categorie.deleteNote(position);
        if(deletedNote != null){
            mAdapter.notifyItemRemoved(position);
            if (mAdapter.getItemCount() == 0){
                placeholderVisible = true;
                placeholder.setVisibility(View.VISIBLE);
                animator.start();
            }
            if (onDeleteNoteListener != null)
                onDeleteNoteListener.onDeleteNote(this.categorie, deletedNote);
        }
        return deletedNote != null;
    }

    public String getName(){
        return categorie.getName();
    }

    public boolean pushNote(String name){
        INote pushedNote = categorie.addNote(name);
        placeholderVisible = false;
        if (mAdapter != null){
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
            if (!placeholderVisible){
                animator.cancel();
                placeholder.setVisibility(View.INVISIBLE);
            }
            if (onAddNoteListener != null && pushedNote != null)
                onAddNoteListener.onAddNote(this.categorie, pushedNote);
        }
        return pushedNote != null;
    }

    public void setOnDeleteNoteListener(OnDeleteNoteListener listener){
        this.onDeleteNoteListener = listener;
    }

    public void setOnAddNoteListener(OnAddNoteListener listener){
        this.onAddNoteListener = listener;
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
    public NoteImpl(){ note = ""; }

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