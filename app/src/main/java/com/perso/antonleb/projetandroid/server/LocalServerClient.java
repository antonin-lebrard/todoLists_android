package com.perso.antonleb.projetandroid.server;

import com.perso.antonleb.projetandroid.data.CategoryKey;
import com.perso.antonleb.projetandroid.data.ICategory;
import com.perso.antonleb.projetandroid.data.IUser;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public class LocalServerClient implements INoteServerClient {
    @Override
    public IUser getUser(String identifier) {
        return null;
    }

    @Override
    public void addNote(CategoryKey categoryKey, String note) {

    }

    @Override
    public void removeNote(CategoryKey categoryKey, String note) {

    }

    @Override
    public void removeCategory(ICategory category) {

    }

    @Override
    public ICategory getCategory(CategoryKey key) {
        return null;
    }
}
