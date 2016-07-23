package com.bo.cloudshoppinglist.activity;

import android.app.Application;

import com.bo.cloudshoppinglist.R;
import com.parse.Parse;

/**
 * Created by bowang on 28/03/15.
 */
public class CloudShoppingListApplication extends Application {

    public static String ERROR__EMPTY_SHOP_LIST_NAME;
    public static String ERROR_NETWORK_ERROR;
    public static String ERROR_EMAIL_ADDRESS;
    public static String ERROR_CANNOT_FOUND;
    public static String RESET_EMAIL_INFO;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "yourkey", "youkey");

        ERROR__EMPTY_SHOP_LIST_NAME = getResources().getString(R.string.list_name_cannot_be_empty);
        ERROR_NETWORK_ERROR = getResources().getString(R.string.network_error);
        ERROR_EMAIL_ADDRESS = getResources().getString(R.string.email_cannot_be_empty);
        ERROR_CANNOT_FOUND = getResources().getString(R.string.no_result_found);
        RESET_EMAIL_INFO = getResources().getString(R.string.reset_password);
    }
}
