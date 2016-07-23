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
import com.parse.ParseUser;

/**
 * Created by bowang on 28/03/15.
 */
public class AddShoppingListActivity extends Activity {

    private EditText shopListName;
    private Button submitBtn;
    private TextView backBtn;

    private ProgressBar progressBar;
    private ParseObject parseObject;
    private TextView addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_shopping_list);
        setupUI(findViewById(R.id.add_shopping_list_id));

        shopListName = (EditText) findViewById(R.id.editTextShopListName);
        submitBtn = (Button) findViewById(R.id.buttonSave);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        backBtn = (TextView) findViewById(R.id.imageButtonBack);
        addBtn = (TextView) findViewById(R.id.imageButtonAdd);
        addBtn.setVisibility(View.GONE);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveShoppingList();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        parseObject = Constant.shopList;
        if (parseObject!=null){
            shopListName.setText((String)parseObject.get(Constant.ShoppingList.COLUMN_NAME));
        }
    }



    private void back() {

        finish();
    }

    private void saveShoppingList() {
        if (shopListName.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR__EMPTY_SHOP_LIST_NAME, Toast.LENGTH_LONG).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        if (parseObject == null) {
            parseObject = new ParseObject(Constant.ShoppingList.TABLE_NAME);

            parseObject.put(Constant.ShoppingList.COLUMN_NAME, shopListName.getText().toString());
            parseObject.put(Constant.ShoppingList.COLUMN_USER, ParseUser.getCurrentUser());
            parseObject.put(Constant.ShoppingList.COLUMN_SHARED, "NO");
            parseObject.put(Constant.ShoppingList.COLUMN_CREATE_TIME, Constant.getCurrentDateString());
        } else {
            parseObject.put(Constant.ShoppingList.COLUMN_NAME, shopListName.getText().toString());
        }
        try {
            parseObject.save();

            finish();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_NETWORK_ERROR, Toast.LENGTH_LONG).show();
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
                    hideSoftKeyboard(AddShoppingListActivity.this);
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
