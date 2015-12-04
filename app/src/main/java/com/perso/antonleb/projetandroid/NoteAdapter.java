package com.perso.antonleb.projetandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.perso.antonleb.projetandroid.datas.ICategory;
import com.perso.antonleb.projetandroid.holders.CategoryFragmentHolder;

/**
 * Created by antonleb on 09/11/2015.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public ICategory category;

    private CategoryFragmentHolder inWithin = null;

    private static String urlPrefix = "https://www.google.com/search?q=";

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        public TextView mNote;
        public ImageButton mGoogle;
        public ImageButton mDelete;
        public NoteViewHolder(View v) {
            super(v);
            mNote = (TextView) v.findViewById(R.id.note);
            mGoogle = (ImageButton) v.findViewById(R.id.google);
            mDelete = (ImageButton) v.findViewById(R.id.delete);
        }
    }

    public NoteAdapter(CategoryFragmentHolder inWithin, ICategory category) {
        this.inWithin = inWithin;
        this.category = category;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        NoteViewHolder vh = new NoteViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
        final String note = category.getNote(position);
        holder.mNote.setText(note);
        holder.mGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlPrefix + holder.mNote.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                inWithin.startActivity(i);
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inWithin.deleteNote(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return category.size();
    }
}

