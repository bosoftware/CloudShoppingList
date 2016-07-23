package com.bo.cloudshoppinglist.activity;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bowang on 28/03/15.
 */
public final class Constant {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static String SHOP_LIST_ITEM_ID = "shopListItemId";
    public static String SHOP_LIST_ID = "shopListId";
    public static ParseObject shopList;
    public static ParseObject shopListItem;
    public static ParseObject shopListItemPhoto;

    public static ParseObject friend;

    public static class ShoppingList{
        public  static final String TABLE_NAME= "shopList";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_CREATE_TIME = "createTime";
        public static final String COLUMN_SHARED = "shared";

    }

    public static class ShoppingListItem{
        public static final String TABLE_NAME = "shopListItem";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_SHOP_LIST = "shopList";
        public static final String COLUMN_AMOUNT="amount";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_MEMO = "memo";
        public static final String COLUMN_ISBOUGHT = "isbought";
    }

    public static class ShoppingItemPhoto{
        public static final String TABLE_NAME="itemPhoto";
        public static final String COLUMN_IMAGE_NAME = "imageName";
        public static final String COLUMN_IMAGE_FILE = "imageFile";
        public static final String COLUMN_SHOP_LIST_ITEM = "shopListItem";
        public static final String COLUMN_SHOP_LIST = "shopList";
        public static final String COLUMN_IMAGE_FILE_NAME = "image.png";
    }

    public static class Friends{
        public static final String TABLE_NAME = "friends";
        public static final String COLUMN_USER = "User";
        public static final String COLUMN_FRIEND = "friend";
    }


    public static  String getCurrentDateString(){
        return simpleDateFormat.format(new Date());
    }
}
