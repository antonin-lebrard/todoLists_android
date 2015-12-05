package com.perso.antonleb.projetandroid.listeners;

import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.UserKey;

/**
 * Created by CEDRIC on 05/12/2015.
 */
public interface UserLoadingListener
{
    public void onUserLoaded(IUser user);
    public void onUserLoadingFail(UserKey key);
    public void onUserLoadingSuccess(UserKey key);
}
