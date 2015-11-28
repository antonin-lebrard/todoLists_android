package com.perso.antonleb.projetandroid.server;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.perso.antonleb.projetandroid.data.CategoryKey;
import com.perso.antonleb.projetandroid.data.ICategory;
import com.perso.antonleb.projetandroid.data.IUser;
import com.perso.antonleb.projetandroid.data.User;
import com.perso.antonleb.projetandroid.exceptions.ServerRequestException;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Un client vers un serveur qui respecte les standards d'Antonin.
 */
public class AntoninServerClient implements INoteServerClient {

    protected final NetHttpTransport netHttpTransport;
    protected final HttpRequestFactory httpRequestFactory;
    protected final JsonFactory jsonFactory;

    protected final String serverLocation;

    /**
     * Créer un nouveau client par défaut.
     */
    public AntoninServerClient()
    {
        this.netHttpTransport = new NetHttpTransport();
        this.httpRequestFactory = this.netHttpTransport.createRequestFactory();
        this.jsonFactory = new GsonFactory();
        this.serverLocation = "http://51.254.201.22:8500";
    }

    /**
     * Créer et configurer son client perso.
     *
     * @param serverLocation Url d'accès au serveur.
     */
    public AntoninServerClient(String serverLocation)
    {
        this.netHttpTransport = new NetHttpTransport();
        this.httpRequestFactory = this.netHttpTransport.createRequestFactory();
        this.jsonFactory = new GsonFactory();
        this.serverLocation = serverLocation;
    }

    protected GenericUrl getUrl(String suffix)
    {
        return new GenericUrl(this.serverLocation + "/" + suffix);
    }

    @Override
    public IUser getUser(String identifier) throws ServerRequestException
    {
        User user = new User(identifier);

        try {
            HttpRequest request = this.httpRequestFactory.buildPostRequest(
                    this.getUrl("fetchAll"),
                    new JsonHttpContent(this.jsonFactory, user.getKey())
            );

            HttpResponse response = request.execute();
        } catch (IOException e) {
            throw new ServerRequestException(e);
        }

        return user;
    }

    @Override
    public void addNote(CategoryKey categoryKey, String note) throws ServerRequestException
    {

    }

    @Override
    public void removeNote(CategoryKey categoryKey, String note) throws ServerRequestException
    {

    }

    @Override
    public void removeCategory(ICategory category) throws ServerRequestException
    {

    }

    @Override
    public ICategory getCategory(CategoryKey key) {
        return null;
    }
}
