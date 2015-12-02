package com.perso.antonleb.projetandroid;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.perso.antonleb.projetandroid.listeners.OnAddNoteListener;
import com.perso.antonleb.projetandroid.listeners.OnDeleteNoteListener;
import com.perso.antonleb.projetandroid.Dialogs.DialogCategory;
import com.perso.antonleb.projetandroid.Dialogs.DialogNote;
import com.perso.antonleb.projetandroid.datas.ICategory;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.services.INoteConsumer;
import com.perso.antonleb.projetandroid.services.NoteService;
import com.perso.antonleb.projetandroid.services.SimpleNoteServiceConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatConsumerActivity implements INoteConsumer
{
    public SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private ImageButton addNote;
    private CoordinatorLayout snackbarCoordinator;

    private String username;

    private boolean isUserLoaded = false;

    protected SimpleNoteServiceConnection noteServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getExtras().getString(ConnectActivity.ARG_CONNECT_USERNAME);

        // Service creation and binding
        noteServiceConnection = new SimpleNoteServiceConnection(this);
        Intent intent = new Intent(this, NoteService.class);
        bindService(intent, noteServiceConnection, BIND_AUTO_CREATE);

        setContentView(R.layout.activity_main);

        snackbarCoordinator = (CoordinatorLayout) findViewById(R.id.snackbar_main_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), viewPager);
        viewPager.setAdapter(mSectionsPagerAdapter);

        addNote = (ImageButton) findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLoaded) {
                    String categorieName = mSectionsPagerAdapter.getCurrentCategorieName();
                    if (categorieName == null){
                        Snackbar snackbar = Snackbar.make(snackbarCoordinator, R.string.error_no_categories, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {
                        DialogFragment dialog = DialogNote.newInstance(categorieName);
                        dialog.show(getSupportFragmentManager(), "NoteDialog");
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(snackbarCoordinator, R.string.error_user_not_logon, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public void onBinderCreated() {
        noteServiceConnection.getService().loadUser(new UserKey(username));
    }

    @Override
    public void onUserLoaded(IUser user) {
        if(user == null){
            Snackbar snackbar = Snackbar.make(snackbarCoordinator, R.string.error_user_not_reachable, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else {
            Log.i(getClass().getCanonicalName(), "LOADED " + user + "... ");

            Iterator<ICategory> categories = user.categoriesIterator();
            while(categories.hasNext()) {
                ICategory category = categories.next();

                mSectionsPagerAdapter.addCategorie(category.getName(), false);
                for(String note : category) {
                    mSectionsPagerAdapter.pushNote(category.getName(), note);
                }
            }
            mSectionsPagerAdapter.setGlobalOnAddNoteListener(new OnAddNoteListener() {
                @Override
                public void onAddNote(ICategorie categorie, INote note) {
                    Log.d("Add Listener", "Category : " + categorie.getName() + " Note : " + note.getNote());
                    /// TODO : Code to insert note on server via noteServiceConnection
                }
            });
            mSectionsPagerAdapter.setGlobalOnDeleteNoteListener(new OnDeleteNoteListener() {
                @Override
                public void onDeleteNote(ICategorie categorie, INote note) {
                    Log.d("Delete Listener", "Category : " + categorie.getName() + " Note : " + note.getNote());
                    /// TODO : Code to delete note on server via noteServiceConnection
                }
            });
            isUserLoaded = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(noteServiceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add_category) {
            if (isUserLoaded){
                DialogFragment dialog = new DialogCategory();
                dialog.show(getSupportFragmentManager(), "CategoryDialog");
            } else {
                Snackbar snackbar = Snackbar.make(snackbarCoordinator, R.string.error_user_not_reachable, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        private ArrayList<CategorieFragment> fragments = new ArrayList<>();
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;
        private ViewPager mViewPager;

        private OnDeleteNoteListener globalOnDeleteNoteListener;
        private OnAddNoteListener globalOnAddNoteListener;

        public SectionsPagerAdapter(FragmentManager fm, ViewPager viewPager) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
            mViewPager = viewPager;
        }
        /**
         * Push a note to the current Categorie (the one visible)
         * @param noteName the name of the note
         */
        public void pushNoteToCurrent(String noteName){
            CategorieFragment current = (CategorieFragment)getFragment(mViewPager.getCurrentItem());
            if (current != null) current.pushNote(noteName);
        }
        /**
         * Get the name of the current Categorie (the one visible)
         * @return the name of the current Categorie
         */
        public String getCurrentCategorieName(){
            CategorieFragment current = (CategorieFragment)getFragment(mViewPager.getCurrentItem());
            return current == null ? null : current.getName();
        }
        /**
         * Add a category fragment, and go to it.
         * @param categorieName the name of the category to add
         */
        public void addCategorie(String categorieName){
            this.addCategorie(categorieName, true);
        }
        /**
         * Add a category fragment.
         * @param categorieName the name of the category to add
         * @param goToIt go to the created fragment
         */
        public void addCategorie(String categorieName, boolean goToIt){
            CategorieFragment newFragment = CategorieFragment.newInstance(categorieName);
            fragments.add(newFragment);
            newFragment.setOnAddNoteListener(globalOnAddNoteListener);
            newFragment.setOnDeleteNoteListener(globalOnDeleteNoteListener);
            this.notifyDataSetChanged();
            if (goToIt)
                mViewPager.setCurrentItem(fragments.size()-1);
        }
        /**
         * Add a note to a category.
         * Should be used only for activity launch
         * @param categorieName the name of the category to add the note into
         * @param noteName the name of the note to add
         */
        public void pushNote(String categorieName, String noteName){
            for (CategorieFragment cat : fragments){
                if (cat.getName().equals(categorieName)){
                    cat.pushNote(noteName);
                    break;
                }
            }
        }
        /**
         * Add a listener on note deletion
         * @param listener the delete listener to apply on each note
         */
        public void setGlobalOnDeleteNoteListener(OnDeleteNoteListener listener){
            this.globalOnDeleteNoteListener = listener;
            for (CategorieFragment cf : fragments){
                cf.setOnDeleteNoteListener(listener);
            }
        }

        /**
         * Add a listener on note insertion
         * @param listener the add listener to apply on each note
         */
        public void setGlobalOnAddNoteListener(OnAddNoteListener listener){
            this.globalOnAddNoteListener = listener;
            for (CategorieFragment cf : fragments){
                cf.setOnAddNoteListener(listener);
            }
        }

        /**== Functioning of the FragmentPagerAdapter  ==**/

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        @Override
        public int getCount() { return fragments.size(); }

        public void clear()
        {
            this.fragments.clear();
            this.mFragmentTags.clear();
            this.notifyDataSetChanged();
        }

        private Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }
    }
}
