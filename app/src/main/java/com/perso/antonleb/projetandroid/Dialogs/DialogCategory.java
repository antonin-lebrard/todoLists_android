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

/**
 * Created by antonleb on 26/11/2015.
 */
public class DialogCategory extends android.support.v4.app.DialogFragment {

    public interface CategoryListener {
        void onCategoryValidation(String category);
    }
    private CategoryListener listener;
    private EditText categoryName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = getActivity().getLayoutInflater().inflate(R.layout.dialog_category, null);
        categoryName = (EditText)root.findViewById(R.id.categoryNameEdit);

        categoryName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (actionId == EditorInfo.IME_ACTION_GO) || (actionId == EditorInfo.IME_ACTION_NEXT)
                        || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    listener.onCategoryValidation(categoryName.getText().toString());
                    getDialog().cancel();
                }
                return false;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.action_add_category);
        builder.setView(root);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCategoryValidation(categoryName.getText().toString());
            }
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryName.postDelayed(new ShowKeyboard(), 300);
    }

    @Override
    public void onAttach(final Activity activity) {
        listener = new CategoryListener() {
            @Override
            public void onCategoryValidation(String categoryName) {
                ((MainActivity)getActivity()).mSectionsPagerAdapter.addCategorie(categoryName);
            }
        };
        super.onAttach(activity);
    }

    private class ShowKeyboard implements Runnable {
        @Override
        public void run() {
            categoryName.setFocusableInTouchMode(true);
            categoryName.requestFocus();
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(categoryName, 0);
        }
    }
}
