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
public class ShoppingItemListAdapter extends BaseAdapter {

	private Context context;
	private List<ParseObject> listItems;
	private int itemViewResource;
	private LayoutInflater listContainer;

	static class ShoppingListItemView {
		public TextView nameTextView;
		public TextView pcsTextView;
        public TextView priceTextView;
		public CheckBox checkBox;

	}

	public ShoppingItemListAdapter(Context context, List<ParseObject> data,
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
					.findViewById(R.id.shop_list_item_name);
			listItemView.pcsTextView = (TextView) convertView
					.findViewById(R.id.shop_list_item_pcs);
            listItemView.priceTextView = (TextView) convertView
                    .findViewById(R.id.shop_list_item_price);


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
		listItemView.nameTextView.setText((String) shopList.get(Constant.ShoppingListItem.COLUMN_NAME));
		listItemView.pcsTextView.setText(((String) shopList.get(Constant.ShoppingListItem.COLUMN_AMOUNT))+"pcs");
        listItemView.priceTextView.setText("$"+(String)shopList.get(Constant.ShoppingListItem.COLUMN_PRICE));
		// listItemView.postcodeTextView.setText(property.getPostcode());

        if (shopList.getString(Constant.ShoppingListItem.COLUMN_ISBOUGHT)!=null) {
            listItemView.checkBox.setChecked(shopList.getString(Constant.ShoppingListItem.COLUMN_ISBOUGHT).equalsIgnoreCase("YES"));
        }else{
            listItemView.checkBox.setChecked(false);
        }
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
                    element.put(Constant.ShoppingListItem.COLUMN_ISBOUGHT,"YES");
                }else{
                    element.put(Constant.ShoppingListItem.COLUMN_ISBOUGHT,"NO");
                }
				element.saveInBackground();
			}
		};
	}



}
