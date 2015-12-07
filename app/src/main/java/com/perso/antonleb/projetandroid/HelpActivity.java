package com.perso.antonleb.projetandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by antonleb on 07/12/2015.
 */
public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final TextView linkSources = (TextView) findViewById(R.id.text_sources_link);
        linkSources.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
