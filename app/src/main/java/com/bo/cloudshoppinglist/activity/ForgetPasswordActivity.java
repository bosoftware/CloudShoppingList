package com.bo.cloudshoppinglist.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bo.cloudshoppinglist.R;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.ParseException;
/**
 * Created by bowang on 28/03/15.
 */
public class ForgetPasswordActivity extends Activity {

    private EditText email;

    private Button submitBtn;
    private TextView backBtn;

    private ProgressBar progressBar;
    private TextView titleView;
    private TextView addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_password);
        setupUI(findViewById(R.id.forgetPassword));
        titleView = (TextView) findViewById(R.id.title);
        titleView.setText(getString(R.string.reset_password));
        email = (EditText) findViewById(R.id.editTextEmail);
        submitBtn = (Button) findViewById(R.id.buttonSubmit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        backBtn = (TextView) findViewById(R.id.imageButtonBack);
        addBtn = (TextView) findViewById(R.id.imageButtonAdd);
        addBtn.setVisibility(View.GONE);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

    }


    private void back() {

        finish();
    }

    private void resetPassword() {
        if (email.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_EMAIL_ADDRESS, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            progressBar.setVisibility(View.VISIBLE);
            ParseUser.requestPasswordResetInBackground(email.getText().toString(),
                    new RequestPasswordResetCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                progressBar.setVisibility(View.GONE);
                                // An email was successfully sent with reset instructions.
                                Toast.makeText(getApplicationContext(), CloudShoppingListApplication.RESET_EMAIL_INFO, Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_CANNOT_FOUND, Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    });



        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_CANNOT_FOUND, Toast.LENGTH_LONG).show();
            return;
        }
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
                    hideSoftKeyboard(ForgetPasswordActivity.this);
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
