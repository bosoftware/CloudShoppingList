package com.bo.cloudshoppinglist.activity;

import android.app.Activity;
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
import com.bo.cloudshoppinglist.activity.adapter.ShoppingListAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bowang on 27/03/15.
 */
public class FriendShopListActivity extends Activity {
    private List<ParseObject> shoppingList = new ArrayList<>();
    private ShoppingListAdapter shoppingListAdapter;
    private ListView shoppingListView;
    private Handler handler;
    private TextView addBtn;
    private TextView backBtn;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    private TextView titleView;
    @Override
    public void onCreate(
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_fragment);

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
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        titleView = (TextView) findViewById(R.id.title);
        titleView.setText(getString(R.string.friends_shop_list));
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        backBtn = (TextView) findViewById(R.id.imageButtonBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        shoppingListView = (ListView) findViewById(R.id.listview_shopping_list);
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingList,
                R.layout.shoppinglist_item);
        shoppingListView.setAdapter(shoppingListAdapter);
        shoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject parseObject = (ParseObject) shoppingListView
                        .getItemAtPosition(position);

                Intent intent = new Intent(view.getContext(),
                        ShopListItemActivity.class);
                Constant.shopList = parseObject;
                startActivity(intent);
            }
        });
        handler = getShoppingListHandler(shoppingListAdapter);
        addBtn = (TextView) findViewById(R.id.imageButtonAdd);
        addBtn.setVisibility(View.GONE);



        registerForContextMenu(shoppingListView);

        refresh();


    }

    private void back() {

        finish();
    }


    private void refresh() {
        swipeContainer.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        new LoadShoppingListTask().execute();
    }




    public Handler getShoppingListHandler(final ShoppingListAdapter adapter) {
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


    public class LoadShoppingListTask extends
            AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(Constant.ShoppingList.TABLE_NAME);
                ParseObject parseObject = Constant.friend;
                ParseUser currentUser = parseObject.getParseUser(Constant.Friends.COLUMN_FRIEND);
                query.whereEqualTo(Constant.ShoppingList.COLUMN_USER, currentUser);
                query.whereEqualTo(Constant.ShoppingList.COLUMN_SHARED,"YES");
                List<ParseObject> list = query.find();
                shoppingList.clear();
                shoppingList.addAll(list);
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
        if (v.getId() == R.id.listview_shopping_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle((String) shoppingList.get(info.position).get(Constant.ShoppingList.COLUMN_NAME));
            String[] menuItems = getResources().getStringArray(R.array.rightmenu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.rightmenu);
        String menuItemName = menuItems[menuItemIndex];
        ParseObject shopList = shoppingList.get(info.position);

        if (menuItemIndex == 0) {
            //Delete
            swipeContainer.setRefreshing(true);
            try {
                shoppingList.remove(shopList);
                shopList.delete();
                shoppingListAdapter.notifyDataSetChanged();
            } catch (Exception ex) {
                Toast.makeText(this, CloudShoppingListApplication.ERROR_NETWORK_ERROR, Toast.LENGTH_LONG).show();
            } finally {
                swipeContainer.setRefreshing(false);
            }
        } else {
            //Edit
            Constant.shopList = shopList;
            Intent intent = new Intent(this,AddShoppingListActivity.class);
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
