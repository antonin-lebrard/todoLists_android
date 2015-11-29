package com.perso.antonleb.projetandroid.server;

import android.util.Log;
import android.webkit.HttpAuthHandler;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.perso.antonleb.projetandroid.datas.Category;
import com.perso.antonleb.projetandroid.datas.CategoryKey;
import com.perso.antonleb.projetandroid.datas.ICategory;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.User;
import com.perso.antonleb.projetandroid.exceptions.ServerRequestException;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected final Gson gson;

    /**
     * Créer un nouveau client par défaut.
     */
    public AntoninServerClient()
    {
        this.netHttpTransport = new NetHttpTransport();
        this.httpRequestFactory = this.netHttpTransport.createRequestFactory();
        this.jsonFactory = new GsonFactory();
        this.serverLocation = "http://51.254.201.22:8500";
        this.gson = new Gson();
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
        this.gson = new Gson();
    }

    /**
     * Retourne une url vers le serveur.
     *
     * @param suffix
     * @return
     */
    protected GenericUrl getUrl(String suffix)
    {
        return new GenericUrl(this.serverLocation + "/" + suffix);
    }

    /**
     * Construit une requête GetAll
     *
     * @param identifier
     * @return
     * @throws IOException
     */
    public HttpRequest makeGetAllRequest(String identifier) throws IOException
    {
        Log.i(getClass().getCanonicalName(), "CREATING POST REQUEST ON /getAll FOR USER <" + identifier + ">");

        Map<String, String> params = new HashMap<String, String>();
        params.put("user", identifier);

        return this.httpRequestFactory.buildPostRequest(
                this.getUrl("getAll"),
                new JsonHttpContent(this.jsonFactory, params)
        );
    }

    /**
     * Parsing simple.
     *
     * @param request
     * @return
     * @throws IOException
     */
    public Map<String, List<String>> doRequest(HttpRequest request) throws IOException
    {
        Log.i(getClass().getCanonicalName(), "EXECUTE REQUEST...");
        HttpResponse response = request.execute();

        if(response.getStatusCode() == 200) {
            Log.i(getClass().getCanonicalName(), "BUILD OBJECT FROM RESPONSE");

            Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
            Map<String, List<String>> result = gson.fromJson(response.parseAsString(), type);

            return result;
        }
        else {
            return null;
        }
    }

    @Override
    public IUser getUser(String identifier) throws ServerRequestException
    {
        User user = new User(identifier);

        try {
            HttpRequest request = this.makeGetAllRequest(identifier);
            Map<String, List<String>> result = this.doRequest(request);

            if(result != null) {
                for(String categoryName : result.keySet()) {
                    ICategory category = new Category(user, categoryName, result.get(categoryName));
                    user.addCategory(category);
                }
            }
            else {
                Log.i(getClass().getCanonicalName(), "ERROR /getAll FOR USER <" + identifier + ">");
                user = null;
            }
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
