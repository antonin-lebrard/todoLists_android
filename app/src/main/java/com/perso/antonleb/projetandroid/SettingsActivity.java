package com.perso.antonleb.projetandroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by antonleb on 08/12/2015.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference_screen);
    }
}
