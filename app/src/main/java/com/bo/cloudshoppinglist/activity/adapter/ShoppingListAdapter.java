package com.bo.cloudshoppinglist.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bo.cloudshoppinglist.R;
import com.bo.cloudshoppinglist.activity.Constant;
import com.parse.ParseObject;

import java.util.List;
/**
 * Created by bowang on 28/03/15.
 */
public class ShoppingListAdapter extends BaseAdapter {

	private Context context;
	private List<ParseObject> listItems;
	private int itemViewResource;
	private LayoutInflater listContainer;

	static class ShoppingListItemView {
		public TextView nameTextView;
		public TextView createTimeTextView;


		public CheckBox checkBox;
		
	}

	public ShoppingListAdapter(Context context, List<ParseObject> data,
			int resource) {
		this.context = context;
		this.listItems = data;
		this.itemViewResource = resource;
		this.listContainer = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return listItems.size();
	}

	@Override
	public Object getItem(int pos) {

		return listItems.get(pos);
	}

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ShoppingListItemView listItemView = null;
		if (convertView == null) {

			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ShoppingListItemView();

			listItemView.nameTextView = (TextView) convertView
					.findViewById(R.id.shop_list_name);
			listItemView.createTimeTextView = (TextView) convertView
					.findViewById(R.id.shop_list_create_time);

			listItemView.checkBox = (CheckBox) convertView
					.findViewById(R.id.check);
			
			convertView.setTag(listItemView);

			listItemView.checkBox
					.setOnCheckedChangeListener(getOnCheckedChangeListener(listItemView));

		} else {
			listItemView = (ShoppingListItemView) convertView.getTag();
		}
		listItemView.checkBox.setTag(listItems.get(position));
		ParseObject shopList = listItems.get(position);
		listItemView.nameTextView.setText((String) shopList.get(Constant.ShoppingList.COLUMN_NAME));
		listItemView.createTimeTextView.setText((String) shopList.get(Constant.ShoppingList.COLUMN_CREATE_TIME));
		// listItemView.postcodeTextView.setText(property.getPostcode());

		listItemView.checkBox.setChecked(shopList.getString(Constant.ShoppingList.COLUMN_SHARED).equalsIgnoreCase("YES"));
		
		return convertView;
	}







	public CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener(
			final ShoppingListItemView listItemView) {
		return new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				ParseObject element = (ParseObject) listItemView.checkBox.getTag();
                if (buttonView.isChecked()){
                    element.put(Constant.ShoppingList.COLUMN_SHARED,"YES");
                }else{
                    element.put(Constant.ShoppingList.COLUMN_SHARED,"NO");
                }
				element.saveInBackground();
			}
		};
	}



}
