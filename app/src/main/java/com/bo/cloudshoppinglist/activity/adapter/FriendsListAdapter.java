package com.bo.cloudshoppinglist.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bo.cloudshoppinglist.R;
import com.bo.cloudshoppinglist.activity.Constant;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;
/**
 * Created by bowang on 28/03/15.
 */
public class FriendsListAdapter extends BaseAdapter {

    private Context context;
    private List<ParseObject> friendsItems;
    private int itemViewResource;
    private LayoutInflater listContainer;

    static class FriendsListItemView {
        public TextView nameTextView;
        public TextView emailTextView;

    }

    public FriendsListAdapter(Context context, List<ParseObject> data,
                              int resource) {
        this.context = context;
        this.friendsItems = data;
        this.itemViewResource = resource;
        this.listContainer = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return friendsItems.size();
    }

    @Override
    public Object getItem(int pos) {

        return friendsItems.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendsListItemView listItemView = null;
        if (convertView == null) {

            convertView = listContainer.inflate(this.itemViewResource, null);

            listItemView = new FriendsListItemView();

            listItemView.nameTextView = (TextView) convertView
                    .findViewById(R.id.friend_list_name);
            listItemView.emailTextView = (TextView) convertView
                    .findViewById(R.id.friend_list_email_address);


            convertView.setTag(listItemView);


        } else {
            listItemView = (FriendsListItemView) convertView.getTag();
        }
        try {
            ParseObject friend = friendsItems.get(position);
            ParseUser parseUser = friend.getParseUser(Constant.Friends.COLUMN_FRIEND);
            parseUser = parseUser.fetchIfNeeded();
            if (parseUser != null && parseUser.getUsername() != null && parseUser.getEmail() != null) {
                listItemView.nameTextView.setText((String) parseUser.getUsername());
                listItemView.emailTextView.setText((String) parseUser.getEmail());
                // listItemView.postcodeTextView.setText(property.getPostcode());
            }
        }catch (Exception ex){

        }
        return convertView;
    }


}
