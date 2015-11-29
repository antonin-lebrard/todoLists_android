package com.perso.antonleb.projetandroid;

import android.content.Intent;
import android.os.Debug;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

    protected SimpleNoteServiceConnection noteServiceConnection;

    protected void createConnectInterface()
    {
        setContentView(R.layout.activity_connect);

        Button button = (Button) findViewById(R.id.button_connect);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) findViewById(R.id.username_edit_text);
                noteServiceConnection.getService().loadUser(new UserKey(text.getText().toString()));
                createLautreTruc();
            }
        });
    }

    protected void createLautreTruc()
    {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), viewPager);
        viewPager.setAdapter(mSectionsPagerAdapter);

        /*mSectionsPagerAdapter.pushCategorie("Movies");
        mSectionsPagerAdapter.pushCategorie("Series");
        mSectionsPagerAdapter.pushCategorie("Other");
        mSectionsPagerAdapter.pushNotes("Movies", "first");
        mSectionsPagerAdapter.pushNotes("Movies", "second");
        mSectionsPagerAdapter.pushNotes("Movies", "third");
        mSectionsPagerAdapter.pushNotes("Movies", "fourth");
        mSectionsPagerAdapter.pushNotes("Movies", "fifth");
        mSectionsPagerAdapter.pushNotes("Movies", "sixth");
        mSectionsPagerAdapter.pushNotes("Series", "Red Farm");
        mSectionsPagerAdapter.pushNotes("Other", "blablabla");
        mSectionsPagerAdapter.pushNotes("Other", "swfdqss");

        mSectionsPagerAdapter.notifyDataSetChanged();*/

        addNote = (ImageButton) findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categorieName = mSectionsPagerAdapter.getCurrentCategorieName();
                DialogFragment dialog = DialogNote.newInstance(categorieName);
                dialog.show(getSupportFragmentManager(), "NoteDialog");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.createConnectInterface();

        // Service TEST
        noteServiceConnection = new SimpleNoteServiceConnection(this);
        Intent intent = new Intent(this, NoteService.class);
        bindService(intent, noteServiceConnection, BIND_AUTO_CREATE);
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
            DialogFragment dialog = new DialogCategory();
            dialog.show(getSupportFragmentManager(), "CategoryDialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUserLoaded(IUser user)
    {
        mSectionsPagerAdapter.clear();
        
        if(user == null) this.createConnectInterface();
        else {
            Log.i(getClass().getCanonicalName(), "LOADED " + user + "... ");

            Iterator<ICategory> categories = user.categoriesIterator();
            while(categories.hasNext()) {
                ICategory category = categories.next();

                mSectionsPagerAdapter.addCategorie(category.getName());
                for(String note : category) {
                    mSectionsPagerAdapter.pushNotes(category.getName(), note);
                }
            }
        }

        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        private ArrayList<CategorieFragment> fragments = new ArrayList<>();
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;
        private ViewPager mViewPager;

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
            fragments.add(CategorieFragment.newInstance(categorieName));
            this.notifyDataSetChanged();
            mViewPager.setCurrentItem(fragments.size()-1);
        }
        // TODO : remove. For test purpose only
        public void pushCategorie(String categorieName){
            fragments.add(CategorieFragment.newInstance(categorieName));
        }
        // TODO: remove. For test purpose only
        public void pushNotes(String categorieName, String noteName){
            for (CategorieFragment cat : fragments){
                if (cat.getName().equals(categorieName)){
                    cat.pushNote(noteName);
                    break;
                }
            }
        }
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
        public Fragment getItem(int position) { return fragments.get(position); }
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
