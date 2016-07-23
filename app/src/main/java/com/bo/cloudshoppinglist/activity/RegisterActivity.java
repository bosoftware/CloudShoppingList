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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by bowang on 26/03/15.
 */
public class RegisterActivity extends Activity {
    private ProgressBar spinner;
    private static String ERROR_ACCOUNT_NAME_EMPTY;
    private static String ERROR_PASSWORD_EMPTY;
    private static String ERROR_EMAIL_EMPTY;
    private EditText userName;
    private EditText password;
    private EditText emailAddress;
    private Button cancelBtn;
    private Button submitBtn;
    private TextView forgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI(findViewById(R.id.register_screen));
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        ERROR_ACCOUNT_NAME_EMPTY = getResources().getString(R.string.account_name_cannot_be_empty);
        ERROR_PASSWORD_EMPTY = getResources().getString(R.string.password_cannot_be_empty);
        ERROR_EMAIL_EMPTY = getResources().getString(R.string.email_cannot_be_empty);
        userName = (EditText) findViewById(R.id.name_edit);
        password = (EditText) findViewById(R.id.psw_edit);
        emailAddress = (EditText) findViewById(R.id.email_address);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void cancel() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void submit() {
        if (userName.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), ERROR_ACCOUNT_NAME_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
        if (password.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), ERROR_PASSWORD_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
        if (emailAddress.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), ERROR_EMAIL_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }
        spinner.setVisibility(View.VISIBLE);
        ParseUser user = new ParseUser();
        user.setUsername(userName.getText().toString().trim());
        user.setPassword(password.getText().toString().trim());
        user.setEmail(emailAddress.getText().toString().trim());
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    spinner.setVisibility(View.GONE);
                    // Hooray! Let them use the app now.
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    return;
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
                    hideSoftKeyboard(RegisterActivity.this);
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

    private void resetPassword() {
        Intent intent = new Intent(this,ForgetPasswordActivity.class);
        startActivity(intent);
    }
}
