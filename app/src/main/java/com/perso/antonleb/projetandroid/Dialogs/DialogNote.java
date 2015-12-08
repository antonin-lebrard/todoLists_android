package com.perso.antonleb.projetandroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.perso.antonleb.projetandroid.MainActivity;
import com.perso.antonleb.projetandroid.R;
import com.perso.antonleb.projetandroid.listeners.NoteValidationListener;

/**
 * Created by antonleb on 23/11/2015.
 */
public class DialogNote extends android.support.v4.app.DialogFragment {

    private NoteValidationListener listener;
    private EditText noteName;

    public static String ARG_HINT_PREFIX = "hintPrefix";
    public static DialogNote newInstance(String hintPrefix){
        Bundle args = new Bundle();
        args.putString(ARG_HINT_PREFIX, hintPrefix);
        DialogNote returning = new DialogNote();
        returning.setArguments(args);
        return returning;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String hintPrefix = getArguments().getString(ARG_HINT_PREFIX);

        hintPrefix = hintPrefix == null ? "Bug" : hintPrefix;
        hintPrefix = hintPrefix.substring(0, 1).toUpperCase() + hintPrefix.substring(1, hintPrefix.length());
        String categoryName = hintPrefix;

        View root = getActivity().getLayoutInflater().inflate(R.layout.dialog_note, null);
        noteName = (EditText)root.findViewById(R.id.noteNameEdit);
        hintPrefix = hintPrefix.charAt(hintPrefix.length()-1) == 's' ? hintPrefix.substring(0, hintPrefix.length()-1): hintPrefix;
        hintPrefix = hintPrefix + " to Add";
        noteName.setHint(hintPrefix);

        noteName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (actionId == EditorInfo.IME_ACTION_GO) || (actionId == EditorInfo.IME_ACTION_NEXT)
                        || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    listener.onNoteValidation(noteName.getText().toString());
                    getDialog().cancel();
                }
                return false;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add a note to " + categoryName);
        builder.setView(root);
        builder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onNoteValidation(noteName.getText().toString());
            }
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        noteName.postDelayed(new ShowKeyboard(), 300);
    }

    @Override
    public void onAttach(final Activity activity) {
        listener = new NoteValidationListener() {
            @Override
            public void onNoteValidation(String noteName) {
                if (!noteName.equals(""))
                    ((MainActivity)getActivity()).mSectionsPagerAdapter.pushNoteToCurrent(noteName);
            }
        };
        super.onAttach(activity);
    }

    private class ShowKeyboard implements Runnable {
        @Override
        public void run() {
            noteName.setFocusableInTouchMode(true);
            noteName.requestFocus();
            if (getDialog() != null) {
                if (getDialog().getWindow() != null) {
                    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(noteName, 0);
                }
            }
        }
    }
}


