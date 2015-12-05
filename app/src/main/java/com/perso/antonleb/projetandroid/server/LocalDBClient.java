package com.perso.antonleb.projetandroid.server;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import com.google.api.client.util.IOUtils;
import com.perso.antonleb.projetandroid.datas.Category;
import com.perso.antonleb.projetandroid.datas.CategoryKey;
import com.perso.antonleb.projetandroid.datas.ICategory;
import com.perso.antonleb.projetandroid.datas.IUser;
import com.perso.antonleb.projetandroid.datas.User;
import com.perso.antonleb.projetandroid.datas.UserKey;
import com.perso.antonleb.projetandroid.datas.creators.SimplePolymorphCreator;
import com.perso.antonleb.projetandroid.exceptions.DBRequestException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 *
 * Client local, stocke les informations dans des fichiers locaux.
 * Toujours accessible, super sympa, fort potentiel et pas chiant.
 */
public class LocalDBClient implements INoteDBClient
{
    protected final Context context;
    protected final HashMap<UserKey, IUser> loadedUsers;

    /**
     * Construire un client vers une DB locale.
     *
     * @param context
     */
    public LocalDBClient(Context context)
    {
        this.context = context;
        this.loadedUsers = new HashMap<>();
    }

    /**
     * Extrait le contenu d'un fichier dans un objet Parcel.
     *
     * @param file
     *
     * @return
     */
    protected Parcel toParcel(File file) throws DBRequestException {
        InputStream in = null;
        try {
            in = this.context.openFileInput(file.getName());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Parcel parcel = Parcel.obtain();

            int next = 0;
            while((next = in.read()) >= 0) {
                bytes.write(next);
            }

            in.close();
            parcel.unmarshall(bytes.toByteArray(), 0, bytes.size());
            parcel.setDataPosition(0);
            return parcel;
        }  catch (Exception e) {
            throw new DBRequestException(e);
        }
    }

    /**
     * Récupère un utilisateur strocké dans un fichier local.
     *
     * @param key Clef permettant d'identifier l'utilisateur.
     *
     * @return Utilisateur chargé.
     */
    @Override
    public IUser getUser(UserKey key) throws DBRequestException {
        // Cache
        if(this.loadedUsers.containsKey(key)) {
            return this.loadedUsers.get(key);
        }
        // Chargement
        else {
            File userFile = new File(this.context.getFilesDir(), "USER_" + key.name);
            IUser loaded = null;

            if (userFile.exists()) {
                SimplePolymorphCreator<IUser> userCreator = SimplePolymorphCreator.getCreator(IUser.class);
                Parcel parcel = this.toParcel(userFile);
                loaded = userCreator.createFromParcel(parcel);
            } else {
                loaded = new User(key.name);
            }

            this.loadedUsers.put(key, loaded);
            return loaded;
        }
    }

    /**
     * Change complètement un utilisateur (sauvegardé par appel à close)
     *
     * @param user
     *
     * @throws DBRequestException
     */
    @Override
    public void setUser(IUser user) throws DBRequestException
    {
        this.loadedUsers.put(user.getKey(), user);
    }

    /**
     * Ajoute une note.
     *
     * @param categoryKey Identifiant de la catégorie.
     * @param note Note à ajouter.
     * @throws DBRequestException
     */
    @Override
    public void addNote(CategoryKey categoryKey, String note) throws DBRequestException
    {
        if(!this.loadedUsers.containsKey(categoryKey.owner)){
            this.getUser(categoryKey.owner);
        }

        this.loadedUsers.get(categoryKey.owner).getCategory(categoryKey.categoryName).addNote(note);
    }

    /**
     * Supprime une note.
     *
     * @param categoryKey Identifiant de la catégorie.
     * @param note Note à supprimer.
     * @throws DBRequestException
     */
    @Override
    public void removeNote(CategoryKey categoryKey, String note) throws DBRequestException
    {
        if(!this.loadedUsers.containsKey(categoryKey.owner)){
            this.getUser(categoryKey.owner);
        }

        this.loadedUsers.get(categoryKey.owner).getCategory(categoryKey.categoryName).removeNote(note);
    }

    /**
     * Supprime une catégorie d'un utilisateur.
     *
     * @param category Catégorie à supprimer.
     * @throws DBRequestException
     */
    @Override
    public void removeCategory(ICategory category) throws DBRequestException
    {
        if(!this.loadedUsers.containsKey(category.getOwner())){
            this.getUser(category.getOwner());
        }

        this.loadedUsers.get(category.getOwner()).destroyCategory(category.getName());
    }

    @Override
    public void createCategory(CategoryKey key) throws DBRequestException {
        if(!this.loadedUsers.containsKey(key.owner)){
            this.getUser(key.owner);
        }

        this.loadedUsers.get(key.owner).addCategory(
                new Category(this.loadedUsers.get(key.owner), key.categoryName)
        );
    }

    /**
     * Sauvegarde les changements localement.
     */
    @Override
    public void close() throws DBRequestException {
        // Sauvegarde
        for(UserKey key : this.loadedUsers.keySet())
        {
            File userFile = new File(this.context.getFilesDir(), "USER_" + key.name);
            Parcel parcel = Parcel.obtain();
            this.loadedUsers.get(key).writeToParcel(parcel, 0);

            try {
                OutputStream output = this.context.openFileOutput(userFile.getName(), context.MODE_PRIVATE);

                byte[] bytes = parcel.marshall();

                output.write(bytes);
                output.flush();
                output.close();
            } catch (Exception e) {
                throw new DBRequestException(e);
            }
        }

        this.loadedUsers.clear();
    }
}
