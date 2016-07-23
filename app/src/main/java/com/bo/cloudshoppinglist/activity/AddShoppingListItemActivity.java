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

import java.util.List;

/**
 * Created by bowang on 29/03/15.
 */
public class AddShoppingListItemActivity extends Activity {

    private ParseObject parseObject;
    private EditText itemNameEditText;
    private EditText amountEditText;
    private EditText priceEditText;
    private EditText memoEditText;
    private TextView backBtn;
    private Button saveBtn;
    private ProgressBar progressBar;
    private TextView addBtn;

    private List<ParseObject> photos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_shopping_item);
        setupUI(findViewById(R.id.shop_list_item_layout));
        itemNameEditText = (EditText) findViewById(R.id.editTextShopListItemName);
        amountEditText = (EditText) findViewById(R.id.editTextShopListItemAmount);
        priceEditText = (EditText) findViewById(R.id.editTextShopListItemPrice);
        memoEditText = (EditText) findViewById(R.id.editTextShopListItemMemo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        addBtn = (TextView) findViewById(R.id.imageButtonAdd);
        addBtn.setVisibility(View.GONE);
        parseObject = Constant.shopListItem;
        if (parseObject != null) {
            itemNameEditText.setText((String) parseObject.get(Constant.ShoppingListItem.COLUMN_NAME));
            amountEditText.setText((String) parseObject.get(Constant.ShoppingListItem.COLUMN_AMOUNT));
            priceEditText.setText((String) parseObject.get(Constant.ShoppingListItem.COLUMN_PRICE));
            memoEditText.setText((String) parseObject.get(Constant.ShoppingListItem.COLUMN_MEMO));
        }
        backBtn = (TextView) findViewById(R.id.imageButtonBack);
        saveBtn = (Button) findViewById(R.id.buttonSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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

    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        if (parseObject == null) {
            parseObject = new ParseObject(Constant.ShoppingListItem.TABLE_NAME);
        }
        parseObject.put(Constant.ShoppingListItem.COLUMN_NAME, itemNameEditText.getText().toString());
        parseObject.put(Constant.ShoppingListItem.COLUMN_SHOP_LIST,Constant.shopList);
        parseObject.put(Constant.ShoppingListItem.COLUMN_PRICE, priceEditText.getText().toString());
        parseObject.put(Constant.ShoppingListItem.COLUMN_AMOUNT, amountEditText.getText().toString());
        parseObject.put(Constant.ShoppingListItem.COLUMN_MEMO, memoEditText.getText().toString());
        try {
            parseObject.save();

            finish();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), CloudShoppingListApplication.ERROR_NETWORK_ERROR, Toast.LENGTH_LONG).show();
            return;
        }
        progressBar.setVisibility(View.GONE);
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
                    hideSoftKeyboard(AddShoppingListItemActivity.this);
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
