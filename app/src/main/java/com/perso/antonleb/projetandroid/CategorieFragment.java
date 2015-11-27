package com.perso.antonleb.projetandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonleb on 09/11/2015.
 */
public class CategorieFragment extends Fragment {

    ICategorie categorie;
    ArrayAdapter<INote> adapter = null;

    ListView list;
    FloatingActionButton button;

    public static String ARG_CATEGORIE_NAME = "categorieName";
    public static String ARG_NOTES = "notes";

    public static CategorieFragment newInstance(String categorieName){
        return newInstance(categorieName, new ArrayList<String>());
    }
    public static CategorieFragment newInstance(String categorieName, ArrayList<String> notes){
        CategorieFragment frag = new CategorieFragment();
        frag.categorie = new CategorieImpl(categorieName);
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_NOTES, notes);
        frag.setArguments(args);
        return frag;
    }

    public CategorieFragment(){}

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible){
            if (getActivity() != null)
                getActivity().setTitle(this.categorie.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categorie, container, false);
        list = (ListView)rootView.findViewById(R.id.listNotes);

        ArrayList<String> notes = getArguments().getStringArrayList(ARG_NOTES);
        if (notes != null)
            for (String note : notes)
                categorie.addNote(note);

        this.adapter = new NoteAdapter(this, R.layout.note, this.categorie.getItems());
        list.setAdapter(adapter);


        View mView = getView();
        if (mView != null) {
            if (this.getUserVisibleHint()) {
                getActivity().setTitle(this.categorie.getName());
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        View mView = getView();
        if (mView != null) {
            if (this.getUserVisibleHint()) {
                getActivity().setTitle(this.categorie.getName());
            }
        }
    }

    public boolean deleteNote(int position){
        boolean res = categorie.deleteNote(position);
        if(res) adapter.notifyDataSetChanged();
        return res;
    }

    public String getName(){
        return categorie.getName();
    }

    // TODO: remove. For test purpose only
    public boolean pushNote(String name){
        boolean res = categorie.addNote(name);
        if (adapter != null) adapter.notifyDataSetChanged();
        return res;
    }

}

class CategorieImpl implements ICategorie {
    String name;
    ArrayList<INote> notes = new ArrayList<INote>();

    public CategorieImpl(String name){ this.name = name; }

    public List<INote> getItems(){ return notes; }
    public String getName() { return this.name; }
    public boolean addNote(String note){
        if (note.equals("")) return false;
        NoteImpl tmp = new NoteImpl(note);
        if (!notes.contains(tmp)){
            notes.add(tmp);
            return true;
        }
        return false;
    }
    public boolean deleteNote(String note){
        if (note.equals("")) return false;
        for (int i = 0; i < notes.size(); i++){
            if (notes.get(i).hashCode() == note.hashCode()){
                notes.remove(i);
                return true;
            }
        }
        return false;
    }
    public boolean deleteNote(int index){
        if (index < notes.size() && index > -1){
            notes.remove(index);
            return true;
        }
        return false;
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