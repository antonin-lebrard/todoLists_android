package com.perso.antonleb.projetandroid.holders;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.perso.antonleb.projetandroid.listeners.OnAddNoteListener;
import com.perso.antonleb.projetandroid.listeners.OnDeleteNoteListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by antonleb on 03/12/2015.
 */
public class CategoriesViewHolderImpl extends FragmentPagerAdapter implements CategoriesViewHolder {

    private String username;

    private ArrayList<CategoryFragmentHolderImpl> fragments = new ArrayList<>();
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;

    private OnDeleteNoteListener globalOnDeleteNoteListener;
    private OnAddNoteListener globalOnAddNoteListener;

    public CategoriesViewHolderImpl(FragmentManager fm, ViewPager viewPager, String username) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<>();
        mViewPager = viewPager;
        this.username = username;
    }
    @Override
    public void pushNoteToCurrent(String noteName){
        CategoryFragmentHolderImpl current = (CategoryFragmentHolderImpl)getFragment(mViewPager.getCurrentItem());
        if (current != null) current.pushNote(noteName);
    }
    @Override
    public String getCurrentCategoryName(){
        CategoryFragmentHolderImpl current = (CategoryFragmentHolderImpl)getFragment(mViewPager.getCurrentItem());
        return current == null ? null : current.getName();
    }
    @Override
    public void addCategory(String categoryName){
        CategoryFragmentHolderImpl newFragment = CategoryFragmentHolderImpl.newInstance(this.username, categoryName);
        fragments.add(newFragment);
        newFragment.setOnAddNoteListener(globalOnAddNoteListener);
        newFragment.setOnDeleteNoteListener(globalOnDeleteNoteListener);
        this.notifyDataSetChanged();
    }
    @Override
    public void addCategory(String categoryName, Collection<String> notes){
        CategoryFragmentHolderImpl newFragment = CategoryFragmentHolderImpl.newInstance(this.username, categoryName, notes);
        fragments.add(newFragment);
        newFragment.setOnAddNoteListener(globalOnAddNoteListener);
        newFragment.setOnDeleteNoteListener(globalOnDeleteNoteListener);
        this.notifyDataSetChanged();
    }
    @Override
    public void addCategory(String categoryName, Iterable<String> notes){
        CategoryFragmentHolderImpl newFragment = CategoryFragmentHolderImpl.newInstance(this.username, categoryName, notes);
        fragments.add(newFragment);
        newFragment.setOnAddNoteListener(globalOnAddNoteListener);
        newFragment.setOnDeleteNoteListener(globalOnDeleteNoteListener);
        this.notifyDataSetChanged();
    }
    /**
     * Add a category fragment, and go to it or not
     * @param categoryName the name of the category to add
     * @param goToIt go to the created fragment
     */
    public void addCategory(String categoryName, boolean goToIt){
        this.addCategory(categoryName);
        if (goToIt)
            mViewPager.setCurrentItem(fragments.size()-1);
    }
    public void addNote(String categoryName, String noteName){
        for (CategoryFragmentHolderImpl cat : fragments){
            if (cat.getName().equals(categoryName)){
                cat.pushNote(noteName);
                break;
            }
        }
    }
    public void setGlobalOnDeleteNoteListener(OnDeleteNoteListener listener){
        this.globalOnDeleteNoteListener = listener;
        for (CategoryFragmentHolderImpl cf : fragments){
            cf.setOnDeleteNoteListener(listener);
        }
    }
    public void setGlobalOnAddNoteListener(OnAddNoteListener listener){
        this.globalOnAddNoteListener = listener;
        for (CategoryFragmentHolderImpl cf : fragments){
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
