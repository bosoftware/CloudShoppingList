package com.bo.cloudshoppinglist.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bo.cloudshoppinglist.R;
import com.bo.cloudshoppinglist.activity.adapter.ShoppingItemListAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bowang on 27/03/15.
 */
public class ShopListItemActivity extends AddPhotoActivity {
    private List<ParseObject> shoppingItemsList = new ArrayList<>();
    private ShoppingItemListAdapter shoppingItemListAdapter;
    private ListView shoppingItemListView;
    private Handler handler;
    private TextView addBtn;
    private TextView backBtn;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar progressBar;
    private static final int CAMERA_REQUEST = 1;
    private TextView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,this);
        setContentView(R.layout.shopping_itemlist_fragment);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refresh();
            }
        });
        titleView = (TextView) findViewById(R.id.title);
        titleView.setText(getString(R.string.shop_list_item));
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        backBtn = (TextView) findViewById(R.id.imageButtonBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        shoppingItemListView = (ListView) findViewById(R.id.listview_shopping_list_item);
        shoppingItemListAdapter = new ShoppingItemListAdapter(this, shoppingItemsList,
                R.layout.shoppingitem_item);
        shoppingItemListView.setAdapter(shoppingItemListAdapter);
        shoppingItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject parseObject = (ParseObject) shoppingItemListView
                        .getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(),
                        AddShoppingListItemActivity.class);
                Constant.shopListItem = parseObject;
                startActivity(intent);
            }
        });
        handler = getShoppingItemListHandler(shoppingItemListAdapter);
        addBtn = (TextView) findViewById(R.id.imageButtonAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShoppingList();
            }
        });
        registerForContextMenu(shoppingItemListView);
        refresh();
    }

    private void back() {

        finish();
    }


    private void refresh() {
        swipeContainer.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        new LoadShoppingItemListTask().execute();
    }

    private void addShoppingList() {
        Intent intent = new Intent(this, AddShoppingListItemActivity.class);
        Constant.shopListItem = null;
        startActivity(intent);

    }


    public Handler getShoppingItemListHandler(final ShoppingItemListAdapter adapter) {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == 0) {
                    adapter.notifyDataSetChanged();

                    swipeContainer.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
    }


    public class LoadShoppingItemListTask extends
            AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(Constant.ShoppingListItem.TABLE_NAME);

                query.whereEqualTo(Constant.ShoppingListItem.COLUMN_SHOP_LIST, Constant.shopList);
                List<ParseObject> list = query.find();
                shoppingItemsList.clear();
                shoppingItemsList.addAll(list);
                Message msg = new Message();
                msg.arg1 = 0;
                handler.sendMessage(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
                return "ERROR";
            }
            return null;
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview_shopping_list_item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle((String) shoppingItemsList.get(info.position).get(Constant.ShoppingList.COLUMN_NAME));
            String[] menuItems = getResources().getStringArray(R.array.itemListRightMenu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.itemListRightMenu);
        String menuItemName = menuItems[menuItemIndex];
        ParseObject shopListItem = shoppingItemsList.get(info.position);
        Constant.shopListItem = shopListItem;
        if (menuItemIndex == 0) {
            //Delete
            swipeContainer.setRefreshing(true);
            try {
                shoppingItemsList.remove(shopListItem);
                shopListItem.delete();
                shoppingItemListAdapter.notifyDataSetChanged();
            } catch (Exception ex) {
                Toast.makeText(this, CloudShoppingListApplication.ERROR_NETWORK_ERROR, Toast.LENGTH_LONG).show();
            } finally {
                swipeContainer.setRefreshing(false);
            }
        } else if (menuItemIndex == 1) {
            //Edit

            Intent intent = new Intent(this, AddShoppingListItemActivity.class);
            startActivity(intent);
        } else if (menuItemIndex == 2) {

            callCamera();
        } else if (menuItemIndex == 3) {

            Intent intent = new Intent(this, PhotoListActivity.class);
            startActivity(intent);

        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
