package com.bo.cloudshoppinglist.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bo.cloudshoppinglist.R;
import com.bo.cloudshoppinglist.activity.Constant;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by bowang on 28/03/15.
 */
public class PhotoListAdapter extends BaseAdapter {

    Context context;
    int layoutResourceId;
    List<ParseObject> data = new ArrayList<ParseObject>();
    private LayoutInflater listContainer;

    static class ImageHolder {
        ImageView imgIcon;
    }

    public PhotoListAdapter(Context context, int resource, List<ParseObject> data) {
        this.context = context;
        this.layoutResourceId = resource;
        this.context = context;
        this.data = data;
        this.listContainer = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder = null;

        if (row == null) {

            row = listContainer.inflate(this.layoutResourceId, null);

            holder = new ImageHolder();

            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);

            row.setTag(holder);


        } else {
            holder = (ImageHolder) row.getTag();
        }

        ParseObject picture = data.get(position);
        ParseFile parseFile = picture.getParseFile(Constant.ShoppingItemPhoto.COLUMN_IMAGE_FILE);
        // convert byte to bitmap take from contact class
        try {
            byte[] outImage = parseFile.getData();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            holder.imgIcon.setImageBitmap(theImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return row;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int index) {
        return 0;
    }


}
