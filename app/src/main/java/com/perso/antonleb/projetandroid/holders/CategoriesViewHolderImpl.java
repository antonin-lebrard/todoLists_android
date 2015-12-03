package com.perso.antonleb.projetandroid.holders;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.perso.antonleb.projetandroid.CategoryFragment;
import com.perso.antonleb.projetandroid.listeners.OnAddNoteListener;
import com.perso.antonleb.projetandroid.listeners.OnDeleteNoteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by antonleb on 03/12/2015.
 */
public class CategoriesViewHolderImpl extends FragmentPagerAdapter implements CategoriesViewHolder {

    private ArrayList<CategoryFragment> fragments = new ArrayList<>();
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;

    private OnDeleteNoteListener globalOnDeleteNoteListener;
    private OnAddNoteListener globalOnAddNoteListener;

    public CategoriesViewHolderImpl(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
        mViewPager = viewPager;
    }

    public void pushNoteToCurrent(String noteName){
        CategoryFragment current = (CategoryFragment)getFragment(mViewPager.getCurrentItem());
        if (current != null) current.pushNote(noteName);
    }
    public String getCurrentCategorieName(){
        CategoryFragment current = (CategoryFragment)getFragment(mViewPager.getCurrentItem());
        return current == null ? null : current.getName();
    }
    public void addCategory(String categoryName){
        CategoryFragment newFragment = CategoryFragment.newInstance(categoryName);
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
        for (CategoryFragment cat : fragments){
            if (cat.getName().equals(categoryName)){
                cat.pushNote(noteName);
                break;
            }
        }
    }
    public void setGlobalOnDeleteNoteListener(OnDeleteNoteListener listener){
        this.globalOnDeleteNoteListener = listener;
        for (CategoryFragment cf : fragments){
            cf.setOnDeleteNoteListener(listener);
        }
    }
    public void setGlobalOnAddNoteListener(OnAddNoteListener listener){
        this.globalOnAddNoteListener = listener;
        for (CategoryFragment cf : fragments){
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
