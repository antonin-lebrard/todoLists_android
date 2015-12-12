package com.perso.antonleb.projetandroid.holders;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.perso.antonleb.projetandroid.ConnectActivity;
import com.perso.antonleb.projetandroid.MainActivity;
import com.perso.antonleb.projetandroid.R;
import com.perso.antonleb.projetandroid.datas.Category;
import com.perso.antonleb.projetandroid.datas.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by antonleb on 09/11/2015.
 */
public class CategoryFragmentHolderImpl extends CategoryFragmentHolder {

    private static String ARG_HELPER_DONE = "isHelperDone";

    private static int durationAnimation = 1000;

    private View placeholder, dimmer;
    private ViewPropertyAnimator animator;

    private ImageView google,delete;
    private TextView tooltip;

    public static CategoryFragmentHolderImpl newInstance(String username, String categoryName){
        return newInstance(username, categoryName, new ArrayList<String>());
    }

    public static CategoryFragmentHolderImpl newInstance(String username, String categoryName, Collection<String> notes){
        CategoryFragmentHolderImpl frag = new CategoryFragmentHolderImpl();
        frag.category = new Category(new User(username), categoryName, notes);
        return frag;
    }
    public static CategoryFragmentHolderImpl newInstance(String username, String categoryName, Iterable<String> notes){
        CategoryFragmentHolderImpl frag = new CategoryFragmentHolderImpl();
        frag.category = new Category(new User(username), categoryName, notes);
        return frag;
    }

    public CategoryFragmentHolderImpl(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        placeholder = rootView.findViewById(R.id.text_placeholder_empty_note);
        dimmer = rootView.findViewById(R.id.dimmer);
        google = (ImageView) rootView.findViewById(R.id.blank_google);
        delete = (ImageView) rootView.findViewById(R.id.blank_delete);
        tooltip = (TextView) rootView.findViewById(R.id.tooltip);

        animator = placeholder.animate().alpha(1.0f).setDuration(500).setStartDelay(500).setInterpolator(new DecelerateInterpolator());

        placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showNoteDialog();
            }
        });

        if (category.size() == 0){
            placeholder.setVisibility(View.VISIBLE);
            animator.start();
        }

        View mView = getView();
        if (mView != null)
            if (this.getUserVisibleHint())
                getActivity().setTitle(capitalize(this.getName()));

        return rootView;
    }

    @Override
    public boolean pushNote(String noteName) {
        boolean added = super.pushNote(noteName);
        if (added && animator != null){
            animator.cancel();
            placeholder.setVisibility(View.INVISIBLE);
            doHelperGoogleAnimation();
        }
        return added;
    }

    @Override
    public boolean deleteNote(String noteName) {
        boolean deleted = super.deleteNote(noteName);
        if (deleted && category.size() == 0){
            placeholder.setVisibility(View.VISIBLE);
            animator.start();
        }
        return deleted;
    }

    @Override
    public boolean deleteNote(int position) {
        boolean deleted = super.deleteNote(position);
        if (deleted && category.size() == 0){
            placeholder.setVisibility(View.VISIBLE);
            animator.start();
        }
        return deleted;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible)
            if (getActivity() != null)
                getActivity().setTitle(capitalize(this.getName()));
    }

    @Override
    public void onResume() {
        super.onResume();
        View mView = getView();
        if (mView != null)
            if (this.getUserVisibleHint())
                getActivity().setTitle(capitalize(this.getName()));
    }

    private String capitalize(String toCapitalize){
        String capitalized = toCapitalize;
        if (capitalized.length() == 1)
            capitalized = capitalized.toUpperCase();
        if (capitalized.length() > 1)
            capitalized = capitalized.substring(0,1).toUpperCase() + capitalized.substring(1);
        return capitalized;
    }

    private void doHelperGoogleAnimation(){
        boolean isHelperAlreadyDone = getContext().getSharedPreferences(ConnectActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getBoolean(ARG_HELPER_DONE, false);
        if (isHelperAlreadyDone)
            return;

        google.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        tooltip.setVisibility(View.VISIBLE);
        ((TransitionDrawable)dimmer.getBackground()).startTransition(durationAnimation);
        tooltip.animate().alpha(1.0f).setDuration(durationAnimation).start();
        tooltip.setText(R.string.tooltip_google);
        google.animate().alpha(1.0f).setDuration(durationAnimation)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        CategoryFragmentHolderImpl.this.mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                CategoryFragmentHolderImpl.this.mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        doHelperDeleteAnimation();
                                        return true;
                                    }
                                });
                            }
                        });
                    }
                }).start();
    }

    private void doHelperDeleteAnimation(){
        google.animate().alpha(0.0f).setDuration(durationAnimation/2).start();
        tooltip.animate().alpha(0.0f).setDuration(durationAnimation/2)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        CategoryFragmentHolderImpl.this.mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                tooltip.setText(R.string.tooltip_delete);
                                delete.animate().alpha(1.0f).setDuration(durationAnimation).start();
                                tooltip.animate().alpha(1.0f).setDuration(durationAnimation).start();
                                CategoryFragmentHolderImpl.this.mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        google.setVisibility(View.INVISIBLE);
                                        delete.setVisibility(View.INVISIBLE);
                                        tooltip.setVisibility(View.INVISIBLE);
                                        dimmer.setVisibility(View.INVISIBLE);
                                        getContext().getSharedPreferences(ConnectActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                                                .edit().putBoolean(ARG_HELPER_DONE, true).commit();
                                        return true;
                                    }
                                });
                            }
                        });
                    }
                }).start();
    }

}