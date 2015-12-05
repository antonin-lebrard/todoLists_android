package com.perso.antonleb.projetandroid.async;

/**
 * Created by CEDRIC on 05/12/2015.
 */
public final class ActionProgress<T>
{
    public final ActionResult result;
    public final T data;

    public ActionProgress(T data, ActionResult result) {
        this.result = result;
        this.data = data;
    }
}
