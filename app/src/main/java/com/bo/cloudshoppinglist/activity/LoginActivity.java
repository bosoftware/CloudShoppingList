package com.bo.cloudshoppinglist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bo.cloudshoppinglist.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by bowang on 26/03/15.
 */
public class LoginActivity extends Activity {

    private EditText userAccount;
    private EditText password;
    private Button registerButton;
    private Button signinButton;
    private TextView forgetPassword;
    private static String ERROR_ACCOUNT_NAME_EMPTY;
    private static String ERROR_PASSWORD_EMPTY;
    private static String ERROR_CANNOT_LOGIN;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.login_screen));
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        ERROR_ACCOUNT_NAME_EMPTY = getResources().getString(R.string.account_name_cannot_be_empty);
        ERROR_PASSWORD_EMPTY = getResources().getString(R.string.password_cannot_be_empty);
        ERROR_CANNOT_LOGIN = getResources().getString(R.string.invalid_account);
        userAccount = (EditText) findViewById(R.id.name_edit);
        password = (EditText) findViewById(R.id.psw_edit);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        registerButton = (Button) findViewById(R.id.regist_btn);
        signinButton = (Button) findViewById(R.id.login_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void resetPassword() {
        Intent intent = new Intent(this,ForgetPasswordActivity.class);
        startActivity(intent);
    }

    private void register() {
        Intent intent = new Intent(this,RegisterActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void signIn() {
        if (userAccount.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), ERROR_ACCOUNT_NAME_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
        if (password.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), ERROR_PASSWORD_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
        spinner.setVisibility(View.VISIBLE);
        ParseUser.logInInBackground(userAccount.getText().toString(), password.getText().toString(), new LogInCallback() {
            public void done(ParseUser user,
                             ParseException e) {
                spinner.setVisibility(View.GONE);
                if (user != null) {
                    // Hooray! The user is logged in.
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(getApplicationContext(), ERROR_CANNOT_LOGIN, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
}
