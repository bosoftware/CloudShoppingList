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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by bowang on 28/03/15.
 */
public class AddFriendActivity extends Activity {

    private EditText friendEmail;

    private Button submitBtn;
    private TextView backBtn;

    private ProgressBar progressBar;
    private ParseObject parseObject;
    private TextView addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);
        setupUI(findViewById(R.id.add_friend_id));

        friendEmail = (EditText) findViewById(R.id.editTextFriendEmail);
        submitBtn = (Button) findViewById(R.id.buttonSave);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        backBtn = (TextView) findViewById(R.id.imageButtonBack);
        addBtn = (TextView) findViewById(R.id.imageButtonAdd);
        addBtn.setVisibility(View.GONE);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFriend();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        parseObject = Constant.friend;
        if (parseObject != null) {
            ParseUser parseUser = parseObject.getParseUser(Constant.Friends.COLUMN_FRIEND);
            friendEmail.setText(parseUser.getEmail());
        }
    }


    private void back() {

        finish();
    }

    private void saveFriend() {
        if (friendEmail.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_EMAIL_ADDRESS, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            progressBar.setVisibility(View.VISIBLE);

            ParseQuery parseQuery = ParseUser.getQuery();
            parseQuery.whereEqualTo("email", friendEmail.getText().toString());
            ParseObject parseObject1 = parseQuery.getFirst();

            if (parseObject == null) {
                parseObject = new ParseObject(Constant.Friends.TABLE_NAME);

                parseObject.put(Constant.Friends.COLUMN_USER, ParseUser.getCurrentUser());
                parseObject.put(Constant.Friends.COLUMN_FRIEND, parseObject1);

            } else {
                parseObject.put(Constant.Friends.COLUMN_FRIEND, parseObject1);
            }

            parseObject.save();

            finish();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_CANNOT_FOUND, Toast.LENGTH_LONG).show();
            return;
        } finally {
            progressBar.setVisibility(View.GONE);
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
                    hideSoftKeyboard(AddFriendActivity.this);
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
