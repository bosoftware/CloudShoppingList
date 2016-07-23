package com.bo.cloudshoppinglist.activity.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bo.cloudshoppinglist.R;
import com.bo.cloudshoppinglist.activity.AddShoppingListActivity;
import com.bo.cloudshoppinglist.activity.CloudShoppingListApplication;
import com.bo.cloudshoppinglist.activity.Constant;
import com.bo.cloudshoppinglist.activity.ShopListItemActivity;
import com.bo.cloudshoppinglist.activity.adapter.ShoppingListAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bowang on 27/03/15.
 */
public class ShopListFragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.shopping_list_fragment, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
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
        titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(getString(R.string.shop_list));
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        backBtn = (TextView) view.findViewById(R.id.imageButtonBack);
        backBtn.setVisibility(View.GONE);
        shoppingListView = (ListView) view.findViewById(R.id.listview_shopping_list);
        shoppingListAdapter = new ShoppingListAdapter(view.getContext(), shoppingList,
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
        addBtn = (TextView) view.findViewById(R.id.imageButtonAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShoppingList();
            }
        });


        registerForContextMenu(shoppingListView);

        refresh();

        return view;
    }



    private void refresh() {
        swipeContainer.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        new LoadShoppingListTask().execute();
    }

    private void addShoppingList() {
        Intent intent = new Intent(getView().getContext(), AddShoppingListActivity.class);
        Constant.shopList = null;
        startActivity(intent);

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
                ParseUser currentUser = ParseUser.getCurrentUser();
                query.whereEqualTo(Constant.ShoppingList.COLUMN_USER, currentUser);
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
                Toast.makeText(this.getView().getContext(), CloudShoppingListApplication.ERROR_NETWORK_ERROR, Toast.LENGTH_LONG).show();
            } finally {
                swipeContainer.setRefreshing(false);
            }
        } else {
            //Edit
            Constant.shopList = shopList;
            Intent intent = new Intent(this.getView().getContext(),AddShoppingListActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
