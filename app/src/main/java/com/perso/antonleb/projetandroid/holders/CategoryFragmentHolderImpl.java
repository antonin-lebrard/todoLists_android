package com.perso.antonleb.projetandroid.holders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;

import com.perso.antonleb.projetandroid.R;
import com.perso.antonleb.projetandroid.datas.Category;
import com.perso.antonleb.projetandroid.datas.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by antonleb on 09/11/2015.
 */
public class CategoryFragmentHolderImpl extends CategoryFragmentHolder {

    private View placeholder;
    private ViewPropertyAnimator animator;

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

        animator = placeholder.animate().alpha(1.0f).setDuration(500).setStartDelay(500).setInterpolator(new DecelerateInterpolator());

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

}