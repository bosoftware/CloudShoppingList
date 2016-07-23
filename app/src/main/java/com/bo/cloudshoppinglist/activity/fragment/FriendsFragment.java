package com.bo.cloudshoppinglist.activity.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bo.cloudshoppinglist.R;
import com.bo.cloudshoppinglist.activity.AddFriendActivity;
import com.bo.cloudshoppinglist.activity.Constant;
import com.bo.cloudshoppinglist.activity.FriendShopListActivity;
import com.bo.cloudshoppinglist.activity.adapter.FriendsListAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bowang on 27/03/15.
 */
public class FriendsFragment extends Fragment {
    private List<ParseObject> friendsList = new ArrayList<>();
    private FriendsListAdapter friendsListAdapter;
    private ListView friendsListView;
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
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
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
        titleView.setText(getString(R.string.friends));
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        backBtn = (TextView) view.findViewById(R.id.imageButtonBack);
        backBtn.setVisibility(View.GONE);
        friendsListView = (ListView) view.findViewById(R.id.listview_friends);
        friendsListAdapter = new FriendsListAdapter(view.getContext(), friendsList,
                R.layout.friendlist_item);
        friendsListView.setAdapter(friendsListAdapter);
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject parseObject = (ParseObject) friendsListView
                        .getItemAtPosition(position);

                Intent intent = new Intent(view.getContext(),
                        FriendShopListActivity.class);
                Constant.friend = parseObject;
                startActivity(intent);
            }
        });
        handler = getFriendsListHandler(friendsListAdapter);
        addBtn = (TextView) view.findViewById(R.id.imageButtonAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendsList();
            }
        });


        registerForContextMenu(friendsListView);

        refresh();

        return view;

    }


    public Handler getFriendsListHandler(final FriendsListAdapter adapter) {
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


    private void refresh() {
        swipeContainer.setRefreshing(true);
        progressBar.setVisibility(View.VISIBLE);
        new LoadFriendsListTask().execute();
    }

    public class LoadFriendsListTask extends
            AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(Constant.Friends.TABLE_NAME);
                ParseUser currentUser = ParseUser.getCurrentUser();
                query.whereEqualTo(Constant.Friends.COLUMN_USER, currentUser);
                List<ParseObject> list = query.find();
                friendsList.clear();
                friendsList.addAll(list);
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


    private void addFriendsList() {
        Intent intent = new Intent(getView().getContext(), AddFriendActivity.class);

        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}



