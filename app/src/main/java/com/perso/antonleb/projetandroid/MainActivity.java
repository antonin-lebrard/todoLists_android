package com.perso.antonleb.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.perso.antonleb.projetandroid.Dialogs.DialogCategory;
import com.perso.antonleb.projetandroid.Dialogs.DialogNote;
import com.perso.antonleb.projetandroid.datas.ICategory;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.holders.CategoriesViewHolderImpl;
import com.perso.antonleb.projetandroid.listeners.NetworkStateListener;
import com.perso.antonleb.projetandroid.listeners.OnAddNoteListener;
import com.perso.antonleb.projetandroid.listeners.OnDeleteNoteListener;
import com.perso.antonleb.projetandroid.listeners.UserLoadingListener;
import com.perso.antonleb.projetandroid.receiver.NetworkStateReceiver;
import com.perso.antonleb.projetandroid.services.NoteDBService;
import com.perso.antonleb.projetandroid.services.NoteDBServiceBinder;
import com.perso.antonleb.projetandroid.services.NoteDBServiceConnection;
import com.perso.antonleb.projetandroid.services.ServiceConnectionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements UserLoadingListener, NetworkStateListener
{
    public SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private ImageButton addNote;
    private NetworkStateReceiver stateReceiver;

    public String username;

    private boolean isUserLoaded = false;
    private Toast globalToast;

    public NoteDBServiceConnection noteServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getExtras().getString(ConnectActivity.ARG_CONNECT_USERNAME);
        final UserLoadingListener userLoadingListener = this;

        // Service creation and binding (penser aux lambdas ???)
        noteServiceConnection = NoteDBServiceConnection.connect(this);
        noteServiceConnection.then(new ServiceConnectionListener<NoteDBServiceBinder>() {
            @Override
            public void onServiceConnected(NoteDBServiceBinder binded) {
                binded.loadUser(new UserKey(username), userLoadingListener);
            }
        });

        setContentView(R.layout.activity_main);

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
                        showToast(R.string.error_no_categories);
                    } else {
                        if (globalToast != null) globalToast.cancel();
                        DialogFragment dialog = DialogNote.newInstance(categoryName);
                        dialog.show(getSupportFragmentManager(), "NoteDialog");
                    }
                } else {
                    showToast(R.string.error_user_not_logon);
                }
            }
        });

        stateReceiver = NetworkStateReceiver.createFor(this, this);
    }

    @Override
    public void onUserLoaded(IUser user) {
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
                noteServiceConnection.getService().addNote(category.getCategoryKey(), note);
            }
        });

        mSectionsPagerAdapter.setGlobalOnDeleteNoteListener(new OnDeleteNoteListener() {
            @Override
            public void onDeleteNote(ICategory category, String note) {
                Log.d("Delete Action", "User: " + category.getOwner().name + " Category: " + category.getName() + " Note: " + note);
                noteServiceConnection.getService().removeNote(category.getCategoryKey(), note);
            }
        });

        isUserLoaded = true;
    }

    @Override
    public void onUserLoadingFail(UserKey key)
    {
        showToast(R.string.error_user_not_reachable);
    }

    @Override
    public void onUserLoadingSuccess(UserKey key)
    {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(noteServiceConnection);
        unregisterReceiver(stateReceiver);
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
        } else if (id == R.id.action_help) {
            if (globalToast != null) globalToast.cancel();
            Intent toHelp = new Intent(MainActivity.this, HelpActivity.class);
            MainActivity.this.startActivity(toHelp);
        } else if (id == R.id.action_add_category) {
            if (isUserLoaded){
                if (globalToast != null) globalToast.cancel();
                DialogFragment dialog = new DialogCategory();
                dialog.show(getSupportFragmentManager(), "CategoryDialog");
            } else {
                showToast(R.string.error_user_not_reachable);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(int resourceId){
        if (globalToast != null) globalToast.cancel();
        globalToast = Toast.makeText(getApplicationContext(), resourceId, Toast.LENGTH_SHORT);
        globalToast.show();
    }

    @Override
    public void onEnteringOnlineMode() {}

    @Override
    public void onEnteringOfflineMode() {}

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
