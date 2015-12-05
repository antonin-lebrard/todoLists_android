package com.perso.antonleb.projetandroid.async;

import android.os.AsyncTask;
import android.util.Log;

import com.perso.antonleb.projetandroid.async.command.AddCategoryCommand;
import com.perso.antonleb.projetandroid.async.command.AddNoteCommand;
import com.perso.antonleb.projetandroid.async.command.ICommand;
import com.perso.antonleb.projetandroid.async.command.RemoveNoteCommand;
import com.perso.antonleb.projetandroid.exceptions.CommandExecutionException;
import com.perso.antonleb.projetandroid.exceptions.DBRequestException;
import com.perso.antonleb.projetandroid.listeners.CommandResultListener;
import com.perso.antonleb.projetandroid.server.INoteDBClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Cédric DEMONGIVERT <cedric.demongivert@gmail.com>
 */
public final class LaunchNoteCommandTask
        extends AsyncTask<ICommand, ActionProgress<ICommand>, Collection<ActionProgress<ICommand>>>
{

    protected final INoteDBClient client;
    protected final List<CommandResultListener> listeners;

    public LaunchNoteCommandTask(INoteDBClient client, CommandResultListener... listener)
    {
        super();
        this.client = client;
        this.listeners = new ArrayList<>(Arrays.asList(listener));
    }

    @Override
    protected void onProgressUpdate(ActionProgress<ICommand>... values) {

    }

    @Override
    protected void onPostExecute(Collection<ActionProgress<ICommand>> actionProgresses) {
        for(ActionProgress<ICommand> command : actionProgresses) {
            for (CommandResultListener listener : this.listeners) {
                if(listener != null) {
                    switch (command.result) {
                        case FAIL:
                            listener.onCommandFail(command.data);
                            break;
                        case SUCCESS:
                            listener.onCommandSuccess(command.data);
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected Collection<ActionProgress<ICommand>> doInBackground(ICommand... params) {
        Collection<ActionProgress<ICommand>> result = new ArrayList<>();

        for(ICommand command : params) {
            ActionProgress<ICommand> progress = null;
            try {
                this.launch(command);
                progress = new ActionProgress<ICommand>(command, ActionResult.SUCCESS);
            } catch (CommandExecutionException e) {
                e.printStackTrace();
                progress = new ActionProgress<ICommand>(command, ActionResult.FAIL);
            }
            result.add(progress);
            this.publishProgress(progress);
        }

        return result;
    }

    public void launch(ICommand command) throws CommandExecutionException {

        if(command instanceof AddCategoryCommand) {
            this.launch((AddCategoryCommand) command);
        }
        else if(command instanceof RemoveNoteCommand) {
            this.launch((RemoveNoteCommand) command);
        }
        else if(command instanceof AddNoteCommand) {
            this.launch((AddNoteCommand) command);
        }
        else {
            throw new CommandExecutionException("Unknown command " + command.toString());
        }
    }

    public void launch(AddCategoryCommand command) throws CommandExecutionException
    {
        try {
            this.client.createCategory(command.getCategory());
        } catch (DBRequestException e) {
            throw new CommandExecutionException(e);
        }
    }

    public void launch(AddNoteCommand command) throws CommandExecutionException
    {
        try {
            this.client.addNote(command.getCategory(), command.getNote());
        } catch (DBRequestException e) {
            throw new CommandExecutionException(e);
        }
    }

    public void launch(RemoveNoteCommand command) throws CommandExecutionException
    {
        try {
            this.client.removeNote(command.getCategory(), command.getNote());
        } catch (DBRequestException e) {
            throw new CommandExecutionException(e);
        }
    }
}
