package com.perso.antonleb.projetandroid;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by antonleb on 09/11/2015.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    public List<INote> notes;

    private CategorieFragment inWithin = null;

    private static String urlPrefix = "https://www.google.com/search?q=";

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mNote;
        public ImageButton mGoogle;
        public ImageButton mDelete;
        public ViewHolder(View v) {
            super(v);
            mNote = (TextView) v.findViewById(R.id.note);
            mGoogle = (ImageButton) v.findViewById(R.id.google);
            mDelete = (ImageButton) v.findViewById(R.id.delete);
        }
    }

    public NoteAdapter(CategorieFragment inWithin, List<INote> objects) {
        this.inWithin = inWithin;
        notes = objects;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        final INote note = notes.get(pos);
        holder.mNote.setText(note.getNote());
        holder.mGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlPrefix + note.getNote();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                inWithin.startActivity(i);
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inWithin.deleteNote(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}

