package com.perso.antonleb.projetandroid;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by antonleb on 09/11/2015.
 */
public class NoteAdapter extends ArrayAdapter<INote> {

    private CategorieFragment inWithin = null;

    private static String urlPrefix = "https://www.google.com/search?q=";

    public NoteAdapter(CategorieFragment inWithin, int resource, List<INote> objects) {
        super(inWithin.getActivity(), resource, objects);
        this.inWithin = inWithin;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View root = convertView;
        if (root == null){
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            root = inflater.inflate(R.layout.note, null);
        }
        final INote note = getItem(position);
        ((TextView)root.findViewById(R.id.note)).setText(note.getNote());
        ((Button)root.findViewById(R.id.google)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlPrefix + note.getNote();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }
        });
        ((Button)root.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inWithin.deleteNote(position);
            }
        });
        return root;
    }
}
