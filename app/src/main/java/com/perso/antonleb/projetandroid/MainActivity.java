package com.perso.antonleb.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;

import com.perso.antonleb.projetandroid.Dialogs.DialogCategory;
import com.perso.antonleb.projetandroid.Dialogs.DialogNote;
import com.perso.antonleb.projetandroid.datas.ICategory;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.holders.CategoriesViewHolderImpl;
import com.perso.antonleb.projetandroid.listeners.OnAddNoteListener;
import com.perso.antonleb.projetandroid.listeners.OnDeleteNoteListener;
import com.perso.antonleb.projetandroid.services.INoteConsumer;
import com.perso.antonleb.projetandroid.services.NoteService;
import com.perso.antonleb.projetandroid.services.SimpleNoteServiceConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

        addNote = (ImageButton) findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLoaded) {
                    String categoryName = mSectionsPagerAdapter.getCurrentCategoryName();
                    if (categoryName == null){
                        Snackbar snackbar = Snackbar.make(snackbarCoordinator, R.string.error_no_categories, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {
                        DialogFragment dialog = DialogNote.newInstance(categoryName);
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

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), viewPager, findViewById(R.id.text_placeholder_empty_fragment), user.getName());
            viewPager.setAdapter(mSectionsPagerAdapter);

            Iterator<ICategory> iterator = user.categoriesIterator();
            ArrayList<ICategory> categories = new ArrayList<>();
            while (iterator.hasNext()){
                categories.add(iterator.next());
            }
            for (ICategory c : categories) {
                mSectionsPagerAdapter.addCategory(c.getName(), c);
            }

            mSectionsPagerAdapter.setGlobalOnAddNoteListener(new OnAddNoteListener() {
                @Override
                public void onAddNote(ICategory category, String note) {
                    Log.d("Add Action", "User: " + category.getOwner().name + " Category: " + category.getName() + " Note: " + note);
                    /// TODO : Code to insert note on server via noteServiceConnection
                }
            });
            mSectionsPagerAdapter.setGlobalOnDeleteNoteListener(new OnDeleteNoteListener() {
                @Override
                public void onDeleteNote(ICategory category, String note) {
                    Log.d("Delete Action", "User: " + category.getOwner().name + " Category: " + category.getName() + " Note: " + note);
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

    public class SectionsPagerAdapter extends CategoriesViewHolderImpl {

        private View placeholder;
        private ViewPropertyAnimator animator;

        public SectionsPagerAdapter(FragmentManager fm, ViewPager viewPager, View placeholder, String username) {
            super(fm, viewPager, username);
            this.placeholder = placeholder;
            this.placeholder.setVisibility(View.VISIBLE);
            this.animator = this.placeholder.animate().alpha(1.0f).setDuration(500).setStartDelay(500).setInterpolator(new DecelerateInterpolator());
            this.animator.start();
        }

        @Override
        public void addCategory(String categoryName) {
            this.animator.cancel();
            placeholder.setVisibility(View.INVISIBLE);
            super.addCategory(categoryName);
        }
        @Override
        public void addCategory(String categoryName, Collection<String> notes){
            this.animator.cancel();
            placeholder.setVisibility(View.INVISIBLE);
            super.addCategory(categoryName, notes);
        }
        @Override
        public void addCategory(String categoryName, Iterable<String> notes){
            this.animator.cancel();
            placeholder.setVisibility(View.INVISIBLE);
            super.addCategory(categoryName, notes);
        }
        @Override
        public void addCategory(String categoryName, boolean goToIt) {
            this.animator.cancel();
            placeholder.setVisibility(View.INVISIBLE);
            super.addCategory(categoryName, goToIt);
        }
    }
}
