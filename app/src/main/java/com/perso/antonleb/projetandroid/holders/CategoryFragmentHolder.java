package com.perso.antonleb.projetandroid.holders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perso.antonleb.projetandroid.NoteAdapter;
import com.perso.antonleb.projetandroid.R;
import com.perso.antonleb.projetandroid.datas.ICategory;
import com.perso.antonleb.projetandroid.listeners.OnAddNoteListener;
import com.perso.antonleb.projetandroid.listeners.OnDeleteNoteListener;

/**
 * Created by antonleb on 04/12/2015.
 */
public abstract class CategoryFragmentHolder extends Fragment implements CategoryHolder {

    protected RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnDeleteNoteListener onDeleteNoteListener;
    private OnAddNoteListener onAddNoteListener;

    protected ICategory category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.listNotes);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        this.mAdapter = new NoteAdapter(this, this.category);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public String getName() { return category.getName(); }

    @Override
    public boolean pushNote(String noteName) {
        String pushedNote = category.addNote(noteName);
        if (mAdapter != null){
            mAdapter.notifyItemInserted(mAdapter.getItemCount());
            if (onAddNoteListener != null && pushedNote != null)
                onAddNoteListener.onAddNote(this.category, pushedNote);
        }
        return pushedNote != null;
    }

    @Override
    public boolean deleteNote(String noteName) {
        int position = 0;
        int len = category.size();
        for (int i = 0; i < len; i++){
            if (category.getNote(i).equals(noteName)){
                position = i;
                break;
            }
        }
        String deletedNote = category.removeNote(noteName);
        if(deletedNote != null){
            mAdapter.notifyItemRemoved(position);
            if (onDeleteNoteListener != null)
                onDeleteNoteListener.onDeleteNote(this.category, deletedNote);
        }
        return deletedNote != null;
    }

    @Override
    public boolean deleteNote(int position) {
        String deletedNote = category.removeNote(position);
        if(deletedNote != null){
            mAdapter.notifyItemRemoved(position);
            if (onDeleteNoteListener != null)
                onDeleteNoteListener.onDeleteNote(this.category, deletedNote);
        }
        return deletedNote != null;
    }

    @Override
    public void setOnDeleteNoteListener(OnDeleteNoteListener listener) {
        this.onDeleteNoteListener = listener;
    }

    @Override
    public void setOnAddNoteListener(OnAddNoteListener listener) {
        this.onAddNoteListener = listener;
    }
}
