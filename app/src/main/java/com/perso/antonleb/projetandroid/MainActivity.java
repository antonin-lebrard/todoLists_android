package com.perso.antonleb.projetandroid;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mSectionsPagerAdapter.pushCategorie("Movies");
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

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<CategorieFragment> fragments = new ArrayList<>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        // TODO: remove. For test purpose only
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
        public Fragment getItem(int position) { return fragments.get(position); }
        @Override
        public int getCount() { return fragments.size(); }
    }
}
