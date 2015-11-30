package com.perso.antonleb.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by antonleb on 26/11/2015.
 */
public class ConnectActivity extends AppCompatActivity {

    public static String ARG_CONNECT_USERNAME = "username";

    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        final CoordinatorLayout snackbarLayout = (CoordinatorLayout) findViewById(R.id.snackbar_connect_text);

        username = (EditText) findViewById(R.id.edit_connect_username);
        Button connect = (Button) findViewById(R.id.button_connect);

        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (actionId == EditorInfo.IME_ACTION_GO) || (actionId == EditorInfo.IME_ACTION_NEXT)
                        || (actionId == EditorInfo.IME_ACTION_SEND)) {

                    if (username.getText().toString().equals("")){
                        Snackbar snackbar = Snackbar.make(snackbarLayout, "The username cannot be empty", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {
                        toMain(username.getText().toString());
                    }
                }
                return false;
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")){
                    Snackbar snackbar = Snackbar.make(snackbarLayout, "The username cannot be empty", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    toMain(username.getText().toString());
                }
            }
        });
    }

    private void toMain(String username){
        Intent toMain = new Intent(ConnectActivity.this, MainActivity.class);
        toMain.putExtra(ARG_CONNECT_USERNAME, username);
        ConnectActivity.this.startActivity(toMain);
    }


}
